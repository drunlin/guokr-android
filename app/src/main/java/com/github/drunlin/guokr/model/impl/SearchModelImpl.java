package com.github.drunlin.guokr.model.impl;

import android.support.annotation.NonNull;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.github.drunlin.guokr.bean.ResponseCode;
import com.github.drunlin.guokr.bean.SearchEntry;
import com.github.drunlin.guokr.model.ListModeBase;
import com.github.drunlin.guokr.model.SearchModel;
import com.github.drunlin.guokr.module.tool.Injector;
import com.github.drunlin.guokr.util.UrlUtil;
import com.github.drunlin.signals.impl.Signal3;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import static com.android.volley.Request.Method.GET;
import static com.android.volley.toolbox.HttpHeaderParser.parseCacheHeaders;
import static com.android.volley.toolbox.HttpHeaderParser.parseCharset;
import static java.util.Collections.sort;

/**
 * 用解析网页的方式获取搜索结果。
 *
 * @author drunlin@outlook.com
 */
public class SearchModelImpl
        extends ListModeBase<SearchEntry, List<SearchEntry>, VolleyError> implements SearchModel {

    private final Object LOCK = new Object();

    /**本次搜索的结果。*/
    private List<SearchEntry> currentEntries;
    /**本次搜索的成功返回数。*/
    private int loadedCount;

    //搜索文章，帖子，问答的链接
    private String articleUrl;
    private String postUrl;
    private String questionUrl;

    public SearchModelImpl(Injector injector) {
        super(injector);
    }

    @Override
    public void search(String query) {
        query = query.trim().replaceAll("\\s", "+");
        //noinspection deprecation
        query = URLEncoder.encode(query).replace("%", "%%");

        articleUrl = String.format("http://m.guokr.com/search/article/?wd=%s", query);
        postUrl = String.format("http://m.guokr.com/search/post/?wd=%s", query);
        questionUrl = String.format("http://m.guokr.com/search/question/?wd=%s", query);

        refresh();
    }

    @Override
    protected void onRequest() {
        currentEntries = new ArrayList<>(30);

        request(articleUrl);
        request(postUrl);
        request(questionUrl);
    }

    private void request(String url) {
        url =  UrlUtil.addQuery(url, "page=%d", offset + 1);
        networkModel.add(new Request<List<SearchEntry>>(GET, url, this::onDeliverError) {
            @Override
            protected Response<List<SearchEntry>> parseNetworkResponse(NetworkResponse response) {
                return parseResponse(response);
            }

            @Override
            protected void deliverResponse(List<SearchEntry> response) {
                onDeliverResult(currentEntries);
            }
        }, this);
    }

    @NonNull
    private Response<List<SearchEntry>> parseResponse(NetworkResponse response) {
        try {
            String html = new String(response.data, parseCharset(response.headers));
            Document document = Jsoup.parse(html);
            Elements elements = document.getElementsByClass("title-detail");
            List<SearchEntry> searches = new ArrayList<>();
            for (Element element : elements) {
                Element item = element.child(0);
                SearchEntry search = new SearchEntry();
                search.url = "http://m.guokr.com" + item.attr("href");
                search.content = item.html();
                search.datetime = element.child(1).child(1).attr("datetime");
                searches.add(search);
            }
            onParseResult(searches);
            return Response.success(searches, parseCacheHeaders(response));
        } catch (Exception e) {
            return Response.error(new ParseError(e));
        }
    }

    @Override
    protected void onParseResult(List<SearchEntry> response) {
        synchronized (LOCK) {
            currentEntries.addAll(response);

            if (++loadedCount == 3) {
                sort(currentEntries, (lhs, rhs) -> getDate(rhs) - getDate(lhs));
            }
        }
    }

    private int getDate(SearchEntry entry) {
        return Integer.parseInt(entry.datetime.replaceAll("\\-", ""));
    }

    @Override
    protected void onDeliverResult(List<SearchEntry> response) {
        synchronized (LOCK) {
            if (loadedCount == 3) {
                super.onDeliverResult(response);
            }
        }
    }

    @Override
    protected void onLoadComplete() {
        super.onLoadComplete();

        loadedCount = 0;
        currentEntries = null;
    }

    @Override
    protected void onDeliverError(VolleyError error) {
        super.onDeliverError(error);

        networkModel.cancel(this);

        resulted.dispatch(ResponseCode.ERROR, false, null);
    }

    @Override
    public void requestRefresh() {
        if (articleUrl != null) {
            refresh();
        }
    }

    @Override
    protected int getNextPageOffset() {
        return offset + 1;
    }

    @Override
    protected boolean canRequestMore() {
        return super.canRequestMore() && result.size() > 0;
    }

    public List<SearchEntry> getSearchList() {
        return result;
    }

    @Override
    public Signal3<Integer, Boolean, List<SearchEntry>> searchResulted() {
        return resulted;
    }
}

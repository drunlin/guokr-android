package com.github.drunlin.guokr.bean;

/**
 * 注册gson工作所需要的Class。
 *
 * @author drunlin@outlook.com
 */
public interface ResultClassMap {
    class PostsResult extends CollectionResult<PostEntry> {}
    class QuestionsResult extends CollectionResult<QuestionEntry> {}
    class ArticlesResult extends CollectionResult<ArticleEntry> {}
    class ArticleCommentsResult extends CollectionResult<Comment> {}
    class ArticleContentResult extends Result<ArticleContent> {}
    class PostContentResult extends Result<PostContent> {}
    class PostCommentsResult extends CollectionResult<Comment> {}
    class QuestionContentResult extends Result<QuestionContent> {}
    class QuestionAnswersResult extends CollectionResult<Answer> {}
    class BasketCategoryResult extends CollectionResult<Basket.Category> {}
    class BasketsResult extends CollectionResult<Basket> {}
    class ImageResult extends Result<Image> {}
    class GroupsResult extends CollectionResult<Group> {}
    class NoticeNumResult extends Result<NoticeNum> {}
    class NoticesResult extends CollectionResult<Notice> {}
    class UserInfoResult extends Result<UserInfo> {}
    class BasketResult extends Result<Basket> {}
}

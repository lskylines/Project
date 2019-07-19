#### <center>项目简介： 项目主要用于为用户提供资讯发布，资讯查看，资讯评论，对评论进行点踩赞
***
+ ***项目技术点***
  + MD5算法对用户密码进行加密，拦截器从Cookie中取出ticket判断用户的登录状态、ThreadLocal保存登录用户的信息、引入PageHelper分页插件实现资讯的分页查看、集成Mybatis操作数据库、使用Redis实现点踩赞功能
***
+ ***开发环境***
   +  IntelliJ IDEA, Java、Spring Boot、Tomcat、MySQL、Redis
***
- *功能简介*
  -    用户注册登录，用户分享资讯、分页查看资讯、查看资讯的评论、分页查看用户个人发帖情况、Redis实现用户对资讯进行点踩赞,有效提高了响应速度,优化了用户体验

### 部分截图
###### 资讯首页
<img src="showImage/index.png">

##### 资讯详情页
<img src="showImage/news.png">

##### 用户发帖首页
<img src="showImage/userIndex.png">

##### 站内信
<img src="showImage/msgList.png">

<img src="showImage/msgdetail.png">

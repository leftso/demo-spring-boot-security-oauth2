# demo-spring-boot-security-oauth2
demo-spring-boot-security-oauth2
该项目是一个基于spring boot 1.4.5整合spring security oauth2 使用jwt方式存储的一个认证服务和资源服务分离的例子demo
1.demo-security-oauth2-authorizationServer 该项目为认证服务，其中也简单集成了一个资源服务
2.demo-security-oauth-resources 该项目是单纯的一个资源服务,用于用户授权成功后拿到token访问有效资源

注意:版本不能升级到1.5.X否则认证成功后访问资源同样会报错401,不知道是不是版本兼容问题

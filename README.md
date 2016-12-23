# releasehelper

APP版本发布系统，用于APP版本管理，检测最新版本。生成下载页面，下载二维码。

Thanks [springside4](https://github.com/springside/springside4)

创建数据库SQL：`/releasehelper/src/main/resources/sql/`

## 使用

App版本下载地址：<http://localhost:8085/releasehelper/download/{app_id}>

App最新版本地址：<http://localhost:8085/releasehelper/api/v1/app/{app_id}>

```
{
  "id" : 14,
  "versionName" : "2.4",
  "versionCode" : 155,
  "intro" : null,
  "createDate" : 1482482346000,
  "updateDate" : 1482482346000,
  "downUrl" : "/static/upload/60335844-2857-4cb2-8d8c-443fde9ec5e2.apk",
  "fileSize" : "3.2 MB",
  "sha1" : "36471e5845a52e6a3cd9edc1acf5f26bf5680ab3",
  "md5" : "df9ab68955dafaa9148e34f7b96e4027",
  "status" : 0,
  "flag" : false
}
```
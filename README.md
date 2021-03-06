# dynamic-sms
支持动态切换短信渠道的短信服务脚手架

## 程序设计

每个渠道具有多条短信发送规则，某个手机号进行业务时，
需要满足所有规则才算是渠道可用，当有一条不满足时，视为渠道不可用。


## 传输方式
使用 HTTP Post 方式，UTF-8 编码，Content-type 设置为 application/x-www-form-urlencoded

## 编码规范
- 报文传输格式使用 JSON，数据编码使用 UTF-8
- 请求时对 JSON 数据进行 Base64 编码后再 URLEncode 编码

## 报文格式

### 请求

| 字段名称 | 字段类型 |	是否可空 |	描述 |
| --- | --- | --- | --- | 
| PhoneNum        |	字符串   | 	N	| 手机号 |
| Content         | 字符串   |	N   | 内容(验证码) |
| SMSType         |	字符串   |	N   | 1：xxxx 2：xxxx 3：xxxx 4：xxxx  |
| Sign            | 字符串   |  Y   | 1：应用一 2： 应用二。不填时，默认为应用一 | 
| Parameters      |	字符串   | Y/N	| 短信模板的参数 当SMSType为2,3,4时不可空 参数1：账号后4位参数2：银行简称 |

示例：（此时还未 Base64 和 URLEncode）

```json
{
    "PhoneNum": "xxxx",
    "Content": "xxxx",
    " SMSType ": "xxxx",
    "Parameters ": [
        "xxxx",
        "xxxx"
    ]
}
```

### 响应

| 字段名称 |	字段类型 |	是否可空 |	描述| 
| --- | --- | --- | --- | 
| Result   |  字符串    |	N |	手机号 | 
| Message  |  字符串	|   N |	内容(验证码)| 

```json
{
    " Result ": true,
    " Message ": "发送成功"
}
```

## 相关链接

**阿里云短信平台：**  
https://help.aliyun.com/product/44282.html?spm=a2c4g.11186623.3.1.Quc7Pv

**腾讯短信平台：**  
https://cloud.tencent.com/document/product/382


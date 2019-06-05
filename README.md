# cmdbuilding-operation

## Introduction
用于对cmdbuilding进行CRUD

## Documentation
- [中文文档](http://www.xuewuzhijing9981.top/cmdbuilding-springboot-starter)

## Features
- 1、对cmdbuilding管理的class中的卡片进行CRUD操作。

## Getting started
maven
```
<dependency>
   <groupId>io.github.whyareyousoseriously</groupId>
   <artifactId>cmdbuilding-springboot-starter</artifactId>
   <version>0.0.1-RELEASE</version>
</dependency>
```
```
cmdb:
  ip: xxxxx
  port: xxx
  username: xxxx
  password: xxxx
```
部分使用如下
```
    @Autowired
    private CmdbuildingTableOperation cmdbuildingTableOperation;

    public void findAllTest(){
        List<CAppInfo> cAppInfos = cmdbuildingTableOperation.find(
                "tablename",
                SessionFactory.makeSessionId(cmdbuildingTableOperation.getCmdbConfig()),
                CAppInfo.class).getData();
    }
    
    public void findPartTest(){
        List<CAppInfo> cAppInfos = cmdbuildingTableOperation.find(
                "tablename",
                CmdbFilter.builder().condition("condition").conditionValue("value").build(),
                SessionFactory.makeSessionId(cmdbuildingTableOperation.getCmdbConfig()),
                CAppInfo.class).getData();
    }
```
## Communication
- [社区交流](http://www.xuewuzhijing9981.top/cmdbuilding-springboot-starter/community.html)

## Contributing
欢迎参与项目贡献！比如提交PR修复一个bug,或新建Issue讨论新特性或者变更。

## Copyright and License
This product is open source and free, and will continue to provide free community technical support. Individual or enterprise users are free to access and use.

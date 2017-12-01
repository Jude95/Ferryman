# Ferryman Android页面路由框架
主要解决项目初具规模后，页面跳转，传参，页面路由等功能代码十分冗余的问题且难以管理的问题。  
主要功能：

1. Android 端页面路由，与 web 页面路由统一，非常便捷的由 web 跳转 activity 页面并携带参数
2. 使用使用自动生成的函数进行 Activity 跳转代码，将页面所需数据作为了函数参数。
3. Activity 返回监听功能，不再需要重写 `onActivityResult` 方法，还能自动装箱返回数据并返回。

支持Kotlin, 支持在 Library 中使用。  
全库没有一个反射，纯依靠 APT 实现。  
使用简洁直观的代码处理页面跳转：
```java
Ferryman.from(MainActivity.this)
        .gotoNameInputActivity(name)
        .onResultWithData(new OnDataResultListener<NameInputActivityResult>() {
            @Override
            public void fullResult(@NonNull NameInputActivityResult data) {
                name = data.getName();
                tvName.setText(data.getName());
            }

            @Override
            public void emptyResult() {
            }

        });
```  
以及使用URL跳转
```java
RouterDriver.startActivity(this,"activity://phoneNumber?name=Lee&country=China");
```
## Dependency

    compile 'com.jude:ferryman-core:1.1.3'
    annotationProcessor 'com.jude:ferryman-compiler:1.1.3'

## Usage

### 1. 页面路由
使用 `@Page` 注解标记Activity。  
默认将使用 Activity 包名作为 URL。  
可以填入页面 URL，进行 URL 路由。一个 Activity 可以有多个地址。一个地址只能对应一个 Activity。  
```java
@Page("activity://two")
public class ActivityTwo extends Activity{
}

//API 方式启动页面
Ferryman.from(ctx).gotoActivityTwo());

//Url 方式启动页面
RouterDriver.startActivity(this,"activity://two");
```
然后就可以使用上面2种优雅的 Activity 跳转方法了。

### 2. 页面参数
使用 `@Params` 注解标记参数。  
在 Activity 中可以直接使用 `Ferryman.unboxingData(this);` 对参数数据拆箱并注入 Activity。    
```java
@Page("activity://phoneNumber")
public class NumberInputActivity extends AppCompatActivity {

    @Params String name;
    @Params("country") String mCountry;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Ferryman.unboxingData(this);
     }
}

//API 方式
Ferryman.from(MainActivity.this).gotoNumberInputActivity("Lee","China");

//Url方式 注意url的参数要经过Encode
RouterDriver.startActivity(this,"activity://phoneNumber?name=Lee&country=China");

```
如果是在 Kotlin 中使用，参数还需要加上 `@JvmField` 注解。
**注解参数支持 DeepLink**可以直接自己构造 DeepLink url进行跳转。api,router,deeplink 三合一
### 3. 页面返回数据
使用 `@Result` 注解标记返回数据。  
使用 `Ferryman.boxingData(this);` 将参数装箱并塞入 Activity。  
```java
@Page
public class NameInputActivity extends AppCompatActivity {

    @Result String name;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Ferryman.unboxingData(this);
    }

    public void submit(){
        name = etName.getText().toString();
        Ferryman.boxingData(this);
        finish();
    }
}

//API 方式
Ferryman.from(MainActivity.this)
        .gotoNameInputActivity()
        .onResultWithData(new OnDataResultListener<NameInputActivityResult>() {
            @Override
            public void fullResult(@NonNull NameInputActivityResult data) {
                name = data.getName();
                tvName.setText(data.getName());
            }

            @Override
            public void emptyResult() {

            }

        });
```
如果是在 Kotlin 中使用，参数还需要加上 `@JvmField` 注解。

### 4. 页面数据注入抽取
参数及返回数据可以定义在非 Activity 类里，只要**与 Activity 建立关联**。  
1. 通过 `@BindActivity` 注解直接关联。
```java
@BindActivity(ShopActivity.class)
public class ShopPresenter {
    @Params
    public String wtf;
    @Params
    public String HUIHJioluHNLI;
    @Params
    public int oooo;
    @Result
    public float yee;

}
```
然后数据的拆箱装箱。  
```java
// 拆箱注入数据
Ferryman.unboxingDataFrom(activity).to(this);
// 装箱保存数据
Ferryman.boxingDataIn(this).to(mActivity);
```
### 5. 自定义路由
允许自己处理未被绑定 Activity 的 url。返回 null 则表示不能处理这个 url。  
```java
FerrymanSetting.addRouterFactory(new Router.Factory() {
    @Override
    public Router createRouter(String url) {
        return null;
    }
});
```
如果可以处理这个 url，则返回处理 Router  
```java
public interface Router {
    Intent start(@NonNull Context context, @NonNull String url);
}
```
### 6. 自定义数据传递序列化
默认提供 Gson 实现的对象序列化，可以添加自定义序列化方式。返回 null 表示不能处理这个类型。  
```java
FerrymanSetting.addConverterFactory(Converter.Factory factory);
```

### 7. URL 拦截器
提供注册拦截器，对需要跳转的url进行一些批处理。
```java
// url 方式的跳转
FerrymanSetting.addUrlInterceptors(RouterInterceptor interceptor);
// api 方式的跳转
FerrymanSetting.addUrlInterceptors(RouterInterceptor interceptor);
```

## Library中使用
Ferryman 可以被使用在 Library 中，Library 中如上正常使用(需要添加 `annotationProcessor`)。
但在 app 与 Library 中都需要添加额外添加一个`ferryman-modular`插件来合并库中自带的路由。
```grovvy
buildscript {
    dependencies {
        classpath 'com.jude:ferryman-modular:1.1.3'
    }
}

apply plugin: 'com.jude.ferryman-modular'

```

License
-------

    Copyright 2017 Jude

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.


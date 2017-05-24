# Ferryman Android页面路由框架

解决项目初具规模后，页面跳转，传参，页面路由等功能代码十分冗余的问题。
主要功能：

1. Android端页面路由，与web页面路由统一，非常便捷的由web跳转activity页面并携带参数
2. 使用使用自动生成的函数进行Activity跳转代码，将页面所需数据作为了函数参数。
3. Activity返回监听功能，不再需要重写`onActivityResult`方法，还能自动装箱返回数据并返回。

全库没有一个反射，纯依靠APT实现。  
使用简洁直观的API处理页面跳转：
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

    compile 'com.jude:ferryman-core:<version>'
    annotationProcessor 'com.jude:ferryman-compiler:<version>'

## Usage

#### 页面路由
使用`@Page`注解标记Activity。  
默认将使用Activity包名作为URL。  
可以填入页面URL，进行URL路由。一个Activity可以有多个地址。一个地址只能对应一个Activity。  
```java
@Page("activity://two")
public class ActivityTwo extends Activity{
}

//API 方式
Ferryman.from(ctx).gotoActivityTwo());

//Url方式
 RouterDriver.startActivity(this,"activity://two");
```
然后就可以使用上面2种优雅的Activity跳转方法了。

#### 参数处理
参数将作为自动生成API的方法参数。  
使用`@Params`注解标记参数。  
在Activity中可以直接使用`Ferryman.unboxingData(this);`对参数数据拆箱并注入Activity。  
`@Params`默认使用变量名作为参数名，也可以指定参数的名字。  
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

//Url方式
 RouterDriver.startActivity(this,"activity://phoneNumber?name=Lee&country=China");

```

#### 返回数据
使用`@Result`注解标记返回数据。使用`Ferryman.boxingData(this);`将参数装箱并塞入Activity。
`@Result`默认使用变量名作为参数名，也可以指定参数的名字。
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
#### 数据注入抽取
参数及返回数据可以定义在非Activity类里，只要与Activity建立关联。  
1. 通过`@BindActivity`注解直接关联。
```java
@BindActivity(ActivityTwo.class)
public class ActivityTwoData {
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
#### 自定义路由
允许自己处理未被绑定Activity的url。返回null则表示不能处理这个url。  
```java
FerrymanSetting.addRouterFactory(new Router.Factory() {
    @Override
    public Router createRouter(String url) {
        return null;
    }
});
```
如果可以处理这个url，则返回处理Router  
```java
public interface Router {
    Intent start(@NonNull Context context, @NonNull String url);
}
```
#### 自定义数据传递序列化
默认提供Gson实现的对象序列化，可以添加自定义序列化方式。返回null表示不能处理这个类型。  
```java
FerrymanSetting.addConverterFactory(new Converter.Factory() {
    @Override
    public Converter createConverter(Type type) {
        return null;
    }
});
```
如果可以处理这个类型，则返回处理Converter  
```java
public interface Converter {
    String encode(Type type,Object object);

    Object decode(Type type,String string);
}
```

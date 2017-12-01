package me.ele.ferryman.core.core;

import com.jude.ferryman.internal.router.Url;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;

/**
 * Created by zane on 2017/1/18.
 */

public class UrlUnitTest {

    @Test
    public void fullBuild() throws Exception {
        Url url = new Url.Builder()
                .setScheme("activity")
                .setAuthority("hello")
                .addParam("name", "hanmeimei")
                .addParam("age", "12")
                .build();
        assertEquals("activity://hello?name=hanmeimei&age=12", url.toString());
    }

    @Test
    public void justSchemeBuild() throws Exception {
        Url url = new Url.Builder()
                .setScheme("activity")
                .addParam("name", "hanmeimei")
                .addParam("age", "12")
                .build();
        assertEquals("activity://?name=hanmeimei&age=12", url.toString());
    }


    @Test
    public void justAuthorityBuild() throws Exception {
        Url url = new Url.Builder()
                .setAuthority("hello")
                .addParam("name", "hanmeimei")
                .addParam("age", "12")
                .build();
        assertEquals("hello?name=hanmeimei&age=12", url.toString());
    }

    @Test
    public void justParamsBuild() throws Exception {
        Url url = new Url.Builder()
                .addParam("name", "hanmeimei")
                .addParam("age", "12")
                .build();
        assertEquals("name=hanmeimei&age=12", url.toString());
    }

    @Test
    public void addressBuild() throws Exception {
        Url url = new Url.Builder()
                .setAddress("what's your name")
                .addParam("name", "hanmeimei")
                .addParam("age", "12")
                .build();
        assertEquals("what's your name?name=hanmeimei&age=12", url.toString());
    }

    @Test
    public void nullBuild() throws Exception {
        Url url = new Url.Builder()
                .build();
        assertEquals("", url.toString());
    }


    public void checkUrl(String urlStr) throws Exception {
        Url url = Url.parse(urlStr);
        assertEquals(urlStr, url.toString());
    }

    public void checkUrl(String urlStr, String format) throws Exception {
        Url url = Url.parse(urlStr);
        assertEquals(format, url.toString());
    }

    @Test
    public void testUrl2() throws Exception {
        String encode = new Url.Builder().addParam("url","https://stackoverflow.com").addParam("url2","https://.com").build().toString();
        Url url = Url.parse(encode);
        System.out.println(url.getParams());
    }

    @Test
    public void invaildSymbol() throws Exception {
        Url url = new Url.Builder()
                .addParam("a",";;//^^")
                .addParam("b","&**)(")
                .setScheme("http")
                .setAuthority("fucker")
                .build();
        assertEquals("http://fucker?a=%3B%3B%2F%2F%5E%5E&b=%26**%29%28",url.toString());
    }

    @Test
    public void testUrl() throws Exception {
        checkUrl("activity://two/xx?a=2&b=3");
        checkUrl("://two/xx?a=2&b=3", "two/xx?a=2&b=3");
        checkUrl("two/xx?a=2&b=3");
        checkUrl("?a=2&b=3", "a=2&b=3");
        checkUrl("a=2&b=3");
        checkUrl("b=3");
        checkUrl("3");
        checkUrl("activity://two/xx?", "activity://two/xx");
        checkUrl("activity://two/xx");
        checkUrl("activity://two");
        checkUrl("activity://");
        checkUrl("activity");
        checkUrl("activity://activity://two/xx?a=2&b=3");
        checkUrl("activity://two/xx?a=2&b=3");
        checkUrl("activity://two/xx?a=2&b=3?c=2&d=3","activity://two/xx?a=2&b=3%3Fc%3D2&d=3");
        checkUrl("activity://two/xx?a=2&b=3&b=2","activity://two/xx?a=2&b=2");
    }
}

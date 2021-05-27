# QUESTIONS

### I have an endpoint comsuming data in `application/x-www-form-urlencoded` format 
### If I send a PUT request like this:


```
curl --request PUT \
  --url http://localhost:18080/user \
  --header 'Content-Type: application/x-www-form-urlencoded;charset=UTF-8' \
  --data name=ken%
```

### As you can see the data `name=ken%` is in illegal url format so I get 500 response and the server side error log:

```
{
  "timestamp": "2021-05-27T02:25:36.849+00:00",
  "status": 500,
  "error": "Internal Server Error",
  "path": "/user"
}
```

```
java.lang.IllegalArgumentException: URLDecoder: Incomplete trailing escape (%) pattern
	at java.net.URLDecoder.decode(URLDecoder.java:187) ~[na:1.8.0_291]
	at org.springframework.http.converter.FormHttpMessageConverter.read(FormHttpMessageConverter.java:356) ~[spring-web-5.3.7.jar:5.3.7]
	at org.springframework.web.filter.FormContentFilter.parseIfNecessary(FormContentFilter.java:109) ~[spring-web-5.3.7.jar:5.3.7]
	at org.springframework.web.filter.FormContentFilter.doFilterInternal(FormContentFilter.java:88) ~[spring-web-5.3.7.jar:5.3.7]
	at org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:119) ~[spring-web-5.3.7.jar:5.3.7]
	at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:189) ~[tomcat-embed-core-9.0.46.jar:9.0.46]
	at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:162) ~[tomcat-embed-core-9.0.46.jar:9.0.46]
	at org.springframework.boot.actuate.metrics.web.servlet.WebMvcMetricsFilter.doFilterInternal(WebMvcMetricsFilter.java:96) ~[spring-boot-actuator-2.5.0-20210520.224402-561.jar:2.5.0-SNAPSHOT]
	at org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:119) ~[spring-web-5.3.7.jar:5.3.7]
	at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:189) ~[tomcat-embed-core-9.0.46.jar:9.0.46]
	at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:162) ~[tomcat-embed-core-9.0.46.jar:9.0.46]
	at org.springframework.web.filter.CharacterEncodingFilter.doFilterInternal(CharacterEncodingFilter.java:201) ~[spring-web-5.3.7.jar:5.3.7]
	at org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:119) ~[spring-web-5.3.7.jar:5.3.7]
	at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:189) ~[tomcat-embed-core-9.0.46.jar:9.0.46]
	at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:162) ~[tomcat-embed-core-9.0.46.jar:9.0.46]
	at org.apache.catalina.core.StandardWrapperValve.invoke(StandardWrapperValve.java:202) ~[tomcat-embed-core-9.0.46.jar:9.0.46]
	at org.apache.catalina.core.StandardContextValve.invoke(StandardContextValve.java:97) [tomcat-embed-core-9.0.46.jar:9.0.46]
	at org.apache.catalina.authenticator.AuthenticatorBase.invoke(AuthenticatorBase.java:542) [tomcat-embed-core-9.0.46.jar:9.0.46]
	at org.apache.catalina.core.StandardHostValve.invoke(StandardHostValve.java:143) [tomcat-embed-core-9.0.46.jar:9.0.46]
	at org.apache.catalina.valves.ErrorReportValve.invoke(ErrorReportValve.java:92) [tomcat-embed-core-9.0.46.jar:9.0.46]
	at org.apache.catalina.core.StandardEngineValve.invoke(StandardEngineValve.java:78) [tomcat-embed-core-9.0.46.jar:9.0.46]
	at org.apache.catalina.connector.CoyoteAdapter.service(CoyoteAdapter.java:357) [tomcat-embed-core-9.0.46.jar:9.0.46]
	at org.apache.coyote.http11.Http11Processor.service(Http11Processor.java:374) [tomcat-embed-core-9.0.46.jar:9.0.46]
	at org.apache.coyote.AbstractProcessorLight.process(AbstractProcessorLight.java:65) [tomcat-embed-core-9.0.46.jar:9.0.46]
	at org.apache.coyote.AbstractProtocol$ConnectionHandler.process(AbstractProtocol.java:893) [tomcat-embed-core-9.0.46.jar:9.0.46]
	at org.apache.tomcat.util.net.NioEndpoint$SocketProcessor.doRun(NioEndpoint.java:1707) [tomcat-embed-core-9.0.46.jar:9.0.46]
	at org.apache.tomcat.util.net.SocketProcessorBase.run(SocketProcessorBase.java:49) [tomcat-embed-core-9.0.46.jar:9.0.46]
	at java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1149) [na:1.8.0_291]
	at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:624) [na:1.8.0_291]
	at org.apache.tomcat.util.threads.TaskThread$WrappingRunnable.run(TaskThread.java:61) [tomcat-embed-core-9.0.46.jar:9.0.46]
	at java.lang.Thread.run(Thread.java:748) [na:1.8.0_291]
```

<br>

### My goal is to let it return 400 status instead of 500, since this error is actually the client's fault not my server's fault
### So I extended the `org.springframework.http.converter.FormHttpMessageConverter` and overrided the `read()` method

<br>

```
public class CustomizedFormHttpMsgConverter extends FormHttpMessageConverter {
    @Override
    public MultiValueMap<String, String> read(@Nullable Class<? extends MultiValueMap<String, ?>> clazz,
            HttpInputMessage inputMessage) throws IOException, HttpMessageNotReadableException {

        MediaType contentType = inputMessage.getHeaders().getContentType();
        Charset charset = (contentType != null && contentType.getCharset() != null ?
                contentType.getCharset() : StandardCharsets.UTF_8);
        String body = StreamUtils.copyToString(inputMessage.getBody(), charset);

        String[] pairs = StringUtils.tokenizeToStringArray(body, "&");
        MultiValueMap<String, String> result = new LinkedMultiValueMap<>(pairs.length);
        for (String pair : pairs) {
            int idx = pair.indexOf('=');
            if (idx == -1) {
                result.add(URLDecoder.decode(pair, charset.name()), null);
            }
            else {
                try {
                    String name = URLDecoder.decode(pair.substring(0, idx), charset.name());
                    String value = URLDecoder.decode(pair.substring(idx + 1), charset.name());
                    result.add(name, value);
                } catch (Throwable th) {
                    System.out.println("HI KEN I GOT HERE!");
                    throw new HttpMessageNotReadableException(th.getMessage());
                }
            }
        }
        return result;
    }
}
```

### Then I registered this customized converter in my configuration class:

```
@Configuration
public class CoreConfig {
    @Bean("customizedFormHttpMsgConverter")
    public FormHttpMessageConverter createMyConverter() {
        return new CustomizedFormHttpMsgConverter();
    }
}
```


### Now I can see my converter is successfully registered through my debug endpoint (/myConfig):

```
{
  "requestMappingHandlerAdapter-converters": [
    "com.ken.demo.CustomizedFormHttpMsgConverter@576c5536",
    "org.springframework.http.converter.ByteArrayHttpMessageConverter@45d20f3d",
    "org.springframework.http.converter.StringHttpMessageConverter@55ecbafe",
    "org.springframework.http.converter.StringHttpMessageConverter@3b567dad",
    "org.springframework.http.converter.ResourceHttpMessageConverter@60ecdde8",
    "org.springframework.http.converter.ResourceRegionHttpMessageConverter@54fac191",
    "org.springframework.http.converter.xml.SourceHttpMessageConverter@10497d4",
    "org.springframework.http.converter.support.AllEncompassingFormHttpMessageConverter@9b7294c",
    "org.springframework.http.converter.json.MappingJackson2HttpMessageConverter@5a8ba37c",
    "org.springframework.http.converter.json.MappingJackson2HttpMessageConverter@f32a60f",
    "org.springframework.http.converter.xml.Jaxb2RootElementHttpMessageConverter@dc61831"
  ],
  "configurers": [
    "org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration$WebMvcAutoConfigurationAdapter@257cc1fc",
    "org.springframework.boot.actuate.autoconfigure.metrics.web.servlet.WebMvcMetricsAutoConfiguration$MetricsWebMvcConfigurer@42e22a53"
  ],
  "converters": [
    "com.ken.demo.CustomizedFormHttpMsgConverter@576c5536",
    "org.springframework.http.converter.StringHttpMessageConverter@55ecbafe",
    "org.springframework.http.converter.json.MappingJackson2HttpMessageConverter@5a8ba37c"
  ]
}
```

<br>

### However, it looks like the CustomizedFormHttpMsgConverter is never used because the illegal request will still result in the same error:

<br>

```
java.lang.IllegalArgumentException: URLDecoder: Incomplete trailing escape (%) pattern
	at java.net.URLDecoder.decode(URLDecoder.java:187) ~[na:1.8.0_291]
	at org.springframework.http.converter.FormHttpMessageConverter.read(FormHttpMessageConverter.java:356) ~[spring-web-5.3.7.jar:5.3.7]
```

<br>

### Of course my debug msg `"HI KEN I GOT THERE"` is never printed out

<br>

## My questions are:
### 1. Why my registered CustomizedFormHttpMsgConverter never get used?
### 2. Is my approach (to extend FormHttpMessageConverter) the right way of doing things? 
### Again my goal is just to change the 500 response to 400 upon URL decoding failures

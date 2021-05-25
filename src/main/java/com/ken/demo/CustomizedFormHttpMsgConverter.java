package com.ken.demo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.lang.Nullable;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StreamUtils;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

@Slf4j
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

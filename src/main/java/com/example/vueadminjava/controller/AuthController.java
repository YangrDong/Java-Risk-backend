package com.example.vueadminjava.controller;

import cn.hutool.core.codec.Base64Encoder;
import cn.hutool.core.map.MapUtil;
import com.example.vueadminjava.common.lang.Const;
import com.example.vueadminjava.common.lang.Result;
import com.example.vueadminjava.entity.SysUser;
import com.google.code.kaptcha.Producer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.Principal;
import java.util.Map;
import java.util.UUID;

@RestController
public class AuthController extends BaseController {

    @Autowired
    Producer producer;

    @GetMapping("/captcha")
    public Result<Map> captcha()throws IOException {
        String key = UUID.randomUUID().toString();
        String code = producer.createText();

        // 将key和code写死，测试用
        key = "aaaaa";
//        code = "11111";

        // 生成验证码图片
//        使用producer对象的createImage()方法生成验证码图片，传入验证码文本作为参数。
        BufferedImage image = producer.createImage(code);
//        创建一个字节输出流，用于将验证码图片写入内存。
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
//        将验证码图片以JPEG格式写入字节输出流中。
        ImageIO.write(image, "jpg", outputStream);

        // 将验证码图片进行序列化，进行传输
//        创建一个Base64Encoder对象，用于将字节数组编码为Base64格式字符串。
        Base64Encoder encoder = new Base64Encoder();
//        定义一个前缀字符串，用于指示传输的数据是Base64编码的JPEG图片。
        String str = "data:image/jpeg;base64,";
//        将验证码图片的字节数组编码为Base64格式，并与前缀字符串拼接，得到完整的Base64编码的验证码图片字符串。
        String base64Img = str + encoder.encode(outputStream.toByteArray());
//        使用redisUtil对象将验证码文本和其对应的唯一标识符作为键值对存储在Redis中，并设置有效期为120秒。
        redisUtil.hset(Const.CAPTCHA_KEY, key, code, 120);
        Result<Map> result = new Result<>();
        result.succ( MapUtil.builder()
                .put("token", key)
                .put("captchaImg", base64Img)
                .build());
        return result;

    }

    @GetMapping("/sys/userInfo")
    public Result<Map> userInfo(Principal principal) {
        SysUser sysUser = sysUserService.getByUsername(principal.getName());
        Result<Map> result = new Result<>();
        result.succ( MapUtil.builder()
                .put("id", sysUser.getId())
                .put("username", sysUser.getUsername())
                .put("avatar", sysUser.getAvatar())
                .put("created", sysUser.getCreated())
                .map());
        return result;
    }

}

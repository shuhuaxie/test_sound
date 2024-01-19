import java.util.*;

public class Main {

    //    接口都已经定义在proto协议文件
//    接口鉴权：
//            1.每个接口请求都包含公共参数，公参放在header中传递
//2.公参包括：deviceToken、sign
//3.将以下参数按照ASCII码排序：应用（appId）、渠道号（channel）、品牌ID（brandId）、型号ID（modelId）、版本号（version）、
//    设备ID（deviceId）、时间戳（timestamp），并按照url参数方式拼接k=v，得到 appId=xxx&brand=xxx&channel=xxx&…… 格式的字符串
//4.将字符串进行base64编码得到deviceToken的值
//5.将字符串拼接秘钥key（appId=xxx&brand=xxx&channel=xxx&……&key=xxx），再进行md5加密，得到sign的值
//
//    第3点 添加一个参数：companyId
//            companyId=3
    // head
    static String[] heads = {"appId", "channel", "brandId", "modelId", "version",
            "deviceId", "timestamp", "companyId"};

    public static void main(String[] args) {
        List<String> ts = Arrays.asList(heads);
        Collections.sort(ts);
        for (int i = 0; i < ts.size(); i++) {
            System.out.print("\"" + ts.get(i) + "\",");
        }
    }
}
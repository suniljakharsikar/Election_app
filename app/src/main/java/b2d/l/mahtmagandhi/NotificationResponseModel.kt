package b2d.l.mahtmagandhi

data class NotificationResponseModel(
        val `data`: List<Data>,
        val message: String,
        val status_code: Int, // 200
        val success: Boolean // true
) {
    data class Data(
        val description: String, // <pre>लॉरेम इप्सम म्हणजे काय? लॉरेम इप्सम हा फक्त मुद्रण आणि टाइपसेटिंग उद्योगाचा बनावट मजकूर आहे. 1500 च्या दशकापासून लोरेम इप्सम हा उद्योगाचा मानक डमी मजकूर आहे.</pre>
        val id: Int, // 5
        val image_name: String, // 81609507113.jpg
        val image_url: String, // /public/upload/81609507113.jpg
        val notification: String // लॉरेम इप्सम म्हणजे काय?
    )
}
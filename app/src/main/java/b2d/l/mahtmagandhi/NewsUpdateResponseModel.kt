package b2d.l.mahtmagandhi

data class NewsUpdateResponseModel(
        val `data`: List<Data>,
        val message: String,
        val status_code: Int, // 200
        val success: Boolean // true
) {
    data class Data(
            var UnlikeStatus: Int, // 0
            var created_at: String, // 2021-02-06 06:31:38
            var description: String, // <p>&nbsp;dolor sit amet, consectetur adipiscing elit. Suspendisse molestie efficitur dolor, et congue ipsum sollicitudin quis. Sed quis malesuada mauris. Nam a tortor aliquet urna porta pretium. Etiam vitae commodo augue, sit amet scelerisque ex. Morbi faucibus lacus ut magna hendrerit, eget convallis velit pharetra. Vestibulum hendrerit, ligula at eleifend feugiat, arcu est malesuada enim, eleifend semper massa massa ut tellus. Aliquam erat volutpat. Aenean aliquam lacus ac purus ornare, at pulvinar magna ullamcorper. Aliquam in porttitor risus. Integer euismod risus eget ex pulvinar, ac hendrerit turpis faucibus. Nullam consequat mi nisi, non lacinia massa vestibulum sed.</p><p>In semper tempor sapien in hendrerit. Duis vitae purus consequat, elementum quam vitae, hendrerit massa. Aliquam accumsan, tortor eget porta vestibulum, ante dolor malesuada elit, ut ornare tellus tellus ut risus. Curabitur mauris est, luctus vitae blandit eu, aliquet eu tortor. Ut nisl metus, tincidunt sit amet placerat in, porta tristique nibh. Maecenas ac auctor libero. Ut imperdiet vitae felis vehicula blandit. Phasellus aliquam in lorem ac tincidunt. In ante tortor, hendrerit id pretium sed, lacinia et ante. I</p><p>&nbsp;dolor sit amet, consectetur adipiscing elit. Suspendisse molestie efficitur dolor, et congue ipsum sollicitudin quis. Sed quis malesuada mauris. Nam a tortor aliquet urna porta pretium. Etiam vitae commodo augue, sit amet scelerisque ex. Morbi faucibus lacus ut magna hendrerit, eget convallis velit pharetra. Vestibulum hendrerit, ligula at eleifend feugiat, arcu est malesuada enim, eleifend semper massa massa ut tellus. Aliquam erat volutpat. Aenean aliquam lacus ac purus ornare, at pulvinar magna ullamcorper. Aliquam in porttitor risus. Integer euismod risus eget ex pulvinar, ac hendrerit turpis faucibus. Nullam consequat mi nisi, non lacinia massa vestibulum sed.</p><p>In semper tempor sapien in hendrerit. Duis vitae purus consequat, elementum quam vitae, hendrerit massa. Aliquam accumsan, tortor eget porta vestibulum, ante dolor malesuada elit, ut ornare tellus tellus ut risus. Curabitur mauris est, luctus vitae blandit eu, aliquet eu tortor. Ut nisl metus, tincidunt sit amet placerat in, porta tristique nibh. Maecenas ac auctor libero. Ut imperdiet vitae felis vehicula blandit. Phasellus aliquam in lorem ac tincidunt. In ante tortor, hendrerit id pretium sed, lacinia et ante. I</p><p>&nbsp;dolor sit amet, consectetur adipiscing elit. Suspendisse molestie efficitur dolor, et congue ipsum sollicitudin quis. Sed quis malesuada mauris. Nam a tortor aliquet urna porta pretium. Etiam vitae commodo augue, sit amet scelerisque ex. Morbi faucibus lacus ut magna hendrerit, eget convallis velit pharetra. Vestibulum hendrerit, ligula at eleifend feugiat, arcu est malesuada enim, eleifend semper massa massa ut tellus. Aliquam erat volutpat. Aenean aliquam lacus ac purus ornare, at pulvinar magna ullamcorper. Aliquam in porttitor risus. Integer euismod risus eget ex pulvinar, ac hendrerit turpis faucibus. Nullam consequat mi nisi, non lacinia massa vestibulum sed.</p><p>In semper tempor sapien in hendrerit. Duis vitae purus consequat, elementum quam vitae, hendrerit massa. Aliquam accumsan, tortor eget porta vestibulum, ante dolor malesuada elit, ut ornare tellus tellus ut risus. Curabitur mauris est, luctus vitae blandit eu, aliquet eu tortor. Ut nisl metus, tincidunt sit amet placerat in, porta tristique nibh. Maecenas ac auctor libero. Ut imperdiet vitae felis vehicula blandit. Phasellus aliquam in lorem ac tincidunt. In ante tortor, hendrerit id pretium sed, lacinia et ante. I</p><p>&nbsp;dolor sit amet, consectetur adipiscing elit. Suspendisse molestie efficitur dolor, et congue ipsum sollicitudin quis. Sed quis malesuada mauris. Nam a tortor aliquet urna porta pretium. Etiam vitae commodo augue, sit amet scelerisque ex. Morbi faucibus lacus ut magna hendrerit, eget convallis velit pharetra. Vestibulum hendrerit, ligula at eleifend feugiat, arcu est malesuada enim, eleifend semper massa massa ut tellus. Aliquam erat volutpat. Aenean aliquam lacus ac purus ornare, at pulvinar magna ullamcorper. Aliquam in porttitor risus. Integer euismod risus eget ex pulvinar, ac hendrerit turpis faucibus. Nullam consequat mi nisi, non lacinia massa vestibulum sed.</p><p>In semper tempor sapien in hendrerit. Duis vitae purus consequat, elementum quam vitae, hendrerit massa. Aliquam accumsan, tortor eget porta vestibulum, ante dolor malesuada elit, ut ornare tellus tellus ut risus. Curabitur mauris est, luctus vitae blandit eu, aliquet eu tortor. Ut nisl metus, tincidunt sit amet placerat in, porta tristique nibh. Maecenas ac auctor libero. Ut imperdiet vitae felis vehicula blandit. Phasellus aliquam in lorem ac tincidunt. In ante tortor, hendrerit id pretium sed, lacinia et ante. I</p>
            var dislike: Int, // 0
            var id: Int, // 26
            var image_name: Any?, // null
            var likeStatus: Int, // 0
            var likes: Int, // 1
            val news_images: List<NewsImage>,
            var title: String, // dolor sit amet, consectetur adipiscing elit. Suspendisse molestie efficitur dolor, et congue ipsum sollicitudin quis. Sed quis malesuada mauris. Nam a tortor aliquet urna porta pretium. Etiam vitae commodo augue, sit amet scelerisque ex. Morbi faucibus la
            var userData: List<UserData>
    ) {
        data class NewsImage(
            val id: Int, // 68
            val image_name: String, // 61612616498.jpg
            val image_url: String, // /public/upload/61612616498.jpg
            val parent_id: Int // 26
        ) {
            override fun toString(): String {
                return image_url
            }
        }

        data class UserData(
            val id: Int, // 8
            val user_image: String, // /public/upload/profile/59457-profilej.jpg
            val user_name: String // sandeeop
        )
    }
}
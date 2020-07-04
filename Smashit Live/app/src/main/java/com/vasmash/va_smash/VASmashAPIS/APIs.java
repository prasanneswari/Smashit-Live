package com.vasmash.va_smash.VASmashAPIS;

public class APIs {

    static String domain_name = "13.235.9.168";  //test
    static String port = "4000"; //test
    static String url = "http"; //test 13.235.9.168:4000/
    // http://13.235.9.168:4000/api/home


    //register
    public static final String sendotp_url = "" + url + "://" + domain_name + ":" + port + "/api/users";
    public static final String submitotp_url = "" + url + "://" + domain_name + ":" + port + "/api/users/verifyOtp";

    public static final String Sendotp = "" + url + "://" + domain_name + ":" + port + "/api/users";
    public static final String Submitotp = "" + url + "://" + domain_name + ":" + port + "/api/users/verifyOtp";
    public static final String Languagesapi = "" + url + "://" + domain_name + ":" + port + "/api/contentlang";
    public static final String Catageriesapi = "" + url + "://" + domain_name + ":" + port + "/api/category";
    public static final String Countrysapi = "" + url + "://" + domain_name + ":" + port + "/api/users/countries";
    public static final String registerprofileapi = "" + url + "://" + domain_name + ":" + port + "/api/users/signup";

    public static final String preference_url = "" + url + "://" + domain_name + ":" + port + "/api/users/preferences";
    public static final String updateprference_url = "" + url + "://" + domain_name + ":" + port + "/api/users/updatePreferences";


    //firebase
    public static final String fcmtoken_url = "" + url + "://" + domain_name + ":" + port + "/api/fcmToken/post";
    public static final String dynamiclink_url = "" + url + "://" + domain_name + ":" + port + "/api/home/getPost?id=";


    //login
    public static final String login_pwdurl = "" + url + "://" + domain_name + ":" + port + "/api/users/signin";
    public static final String loginwithotp_url = "" + url + "://" + domain_name + ":" + port + "/api/users/signinWithOtp";
    public static final String verifyotp_url = "" + url + "://" + domain_name + ":" + port + "/api/users/verifyLoginOtp";
    public static final String resendotp_url = "" + url + "://" + domain_name + ":" + port + "/api/users/resendLoginOtp";
    public static final String forgetpwd_url = "" + url + "://" + domain_name + ":" + port + "/api/users/forgotPassword";
    public static final String forgetotp_url = "" + url + "://" + domain_name + ":" + port + "/api/users/forgotOtpVerify";
    public static final String activeuser_url = "" + url + "://" + domain_name + ":" + port + "/api/users/activateUser";

    public static final String loginwithga_url = "" + url + "://" + domain_name + ":" + port + "/api/users/loginWithGa";
    public static final String checkstatus_url = "" + url + "://" + domain_name + ":" + port + "/api/users/checkStatus";

    public static final String regupload = "" + url + "://" + domain_name + ":" + port + "/api/users/uploadProfilePic?userId=";
    public static final String googlesignin_url = "" + url + "://" + domain_name + ":" + port + "/api/users/google";
    public static final String facebooklogin_url = "" + url + "://" + domain_name + ":" + port + "/api/users/facebook";


    //profile
    public static final String profiledetails_url = "" + url + "://" + domain_name + ":" + port + "/api/users/current";
    public static final String updateprofile_url = "" + url + "://" + domain_name + ":" + port + "/api/users/updateProfile";
    public static final String updateprofilepic_url = "" + url + "://" + domain_name + ":" + port + "/api/users/updateProfilePic";
    public static final String usersliked_url = "" + url + "://" + domain_name + ":" + port + "/api/v2/likes/userLikedVideos";
    public static final String getprofile_url = "" + url + "://" + domain_name + ":" + port + "/api/users/getProfile";
    public static final String usercreated_url = "" + url + "://" + domain_name + ":" + port + "/api/v2/home/userCreatedPosts";
    public static final String userfollowing_url = "" + url + "://" + domain_name + ":" + port + "/api/v2/follow/getFollowing";
    public static final String userfollower_url = "" + url + "://" + domain_name + ":" + port + "/api/v2/follow/getFollowers";
    public static final String userfollow_url = "" + url + "://" + domain_name + ":" + port + "/api/v2/follow/";
    public static final String userunfollow_url = "" + url + "://" + domain_name + ":" + port + "/api/v2/follow/unFollow";


    //other profile
    public static final String otherprofile_url = "" + url + "://" + domain_name + ":" + port + "/api/users/othersProfile?id=";
    public static final String otherprofileuser_url = "" + url + "://" + domain_name + ":" + port + "/api/v2/home/otherUserCreatedPosts?userId=";
    public static final String follow_url = "" + url + "://" + domain_name + ":" + port + "/api/follow/";
    public static final String unfollow_url = "" + url + "://" + domain_name + ":" + port + "/api/follow/unFollow";
    public static final String other_url = "" + url + "://" + domain_name + ":" + port + "/api/users/other?id=";
    public static final String otherollowing_url = "" + url + "://" + domain_name + ":" + port + "/api/v2/follow/getOtherFollowing?id=";
    public static final String otherfollower_url = "" + url + "://" + domain_name + ":" + port + "/api/v2/follow/getOtherFollowers?id=";


    //wallet
    public static final String wallet_url = "" + url + "://" + domain_name + ":" + port + "/api/wallets/";
    public static final String getwallwtbal_url = "" + url + "://" + domain_name + ":" + port + "/api/wallets/walletBalance";
    public static final String p2ptrans_url = "" + url + "://" + domain_name + ":" + port + "/api/wallets/p2pTransactions";
    public static final String getalltransactions_url = "" + url + "://" + domain_name + ":" + port + "/api/wallets/allTransactions";

    //va Content
    public static final String addupload_url = "" + url + "://" + domain_name + ":" + port + "/api/storePosters/uploadPoster";
    public static final String vaadddata_url = "" + url + "://" + domain_name + ":" + port + "/api/storePosters/addData";
    public static final String vastoredata_url = "" + url + "://" + domain_name + ":" + port + "/api/storePosters";
    public static final String vastore_kyc = ""+url+"://"+domain_name+":"+port+"/api/v2/kyc/kycStatus";
    public static final String vastore_uploadDocument = ""+url+"://"+domain_name+":"+port+"/api/v2/kyc/uploadDocument";
    public static final String vastore_addContent = ""+url+"://"+domain_name+":"+port+"/api/v2/kyc/addContent";
    public static final String vastore_userdata = ""+url+"://"+domain_name+":"+port+"/api/v2/storePosters/userPosters";


    public static final String homeapi_url = "" + url + "://" + domain_name + ":" + port + "/api/home";
    public static String homepagination_url = ""+url+"://"+domain_name+":"+port+"/api/v2/home/posts?limit=10&skip=";
    public static final String earningpoints_url = "" + url + "://" + domain_name + ":" + port + "/api/wallets/walletBalance";
    public static final String earningpost_url = "" + url + "://" + domain_name + ":" + port + "/api/wallets/updateSmashBalance";
    public static final String claimsmash_url = "" + url + "://" + domain_name + ":" + port + "/api/wallets/claimSmashCoin";
    public static final String getamount_url = "" + url + "://" + domain_name + ":" + port + "/api/earnCoins/getAmountPerVideo";
    public static final String userreport_url = "" + url + "://" + domain_name + ":" + port + "/api/report/user";
    public static final String postreport_url = "" + url + "://" + domain_name + ":" + port + "/api/report/post";
    public static final String block_url = "" + url + "://" + domain_name + ":" + port + "/api/follow/block";
    public static final String unblock_url = "" + url + "://" + domain_name + ":" + port + "/api/follow/unBlock";
    public static final String hashtags_url = "" + url + "://" + domain_name + ":" + port + "/api/v2/home/tags?tag=%23";
    public static final String deletepost_url = "" + url + "://" + domain_name + ":" + port + "/api/home/delete";
    public static final String visibility_url = "" + url + "://" + domain_name + ":" + port + "/api/home/update";
    public static final String originalsound_url = "" + url + "://" + domain_name + ":" + port + "/api/sounds/audio?id=";

    //comment
    public static final String comment_url = "" + url + "://" + domain_name + ":" + port + "/api/v2/comments/?postId=";
    public static final String posteomment_url = "" + url + "://" + domain_name + ":" + port + "/api/v2/comments/addComment";
    public static final String likescomment_url = "" + url + "://" + domain_name + ":" + port + "/api/likes/likeComments";
    public static final String unlikecomment_url = "" + url + "://" + domain_name + ":" + port + "/api/likes/removeCommentsLike";
    public static final String getfollowing_url = "" + url + "://" + domain_name + ":" + port + "/api/follow/getFollowing";
    public static final String deletcomment_url = "" + url + "://" + domain_name + ":" + port + "/api/comments/delete";
    public static final String reportcomment_url = "" + url + "://" + domain_name + ":" + port + "/api/report/comment";
    public static final String replycomment_url = "" + url + "://" + domain_name + ":" + port + "/api/v2/comments/replyComment";

    //likes
    public static final String getlikes_url = "" + url + "://" + domain_name + ":" + port + "/api/likes/?postId=";
    public static final String likesend_url = "" + url + "://" + domain_name + ":" + port + "/api/likes/like";
    public static final String unlike_url = "" + url + "://" + domain_name + ":" + port + "/api/likes/removeLike";

    //share
    public static final String share_url = "" + url + "://" + domain_name + ":" + port + "/api/home/sharePost?postId=";

    //search
    public static final String search_url = "" + url + "://" + domain_name + ":" + port + "/api/home/search?term=";
    public static final String searchdata_url = "" + url + "://" + domain_name + ":" + port + "/api/home/searchTerms?term=";
    public static final String usersearch_url = "" + url + "://" + domain_name + ":" + port + "/api/v2/home/userSearch?term=";
    public static final String usersound_url = "" + url + "://" + domain_name + ":" + port + "/api/v2/home/searchOnSounds?term=";
    public static final String hashtagsearch_url = "" + url + "://" + domain_name + ":" + port + "/api/v2/home/searchOnTags?term=";
    public static final String searchall_url = "" + url + "://" + domain_name + ":" + port + "/api/v2/home/searchAll?term=";


    //content

    public static final String add_image_content = "" + url + "://" + domain_name + ":" + port + "/api/home/uploadImage";
    public static final String add_video_content = "" + url + "://" + domain_name + ":" + port + "/api/home/uploadVideo";
    public static final String add_description_content = "" + url + "://" + domain_name + ":" + port + "/api/home/updatePost";


    //setting notifications
    public static final String getnotification_url = "" + url + "://" + domain_name + ":" + port + "/api/users/notificationPreferences";


    public static final String Soundapi = ""+url+"://"+domain_name+":"+port+"/api/sounds";
    public static final String Soundapicate = ""+url+"://"+domain_name+":"+port+"/api/audioCategory";
    public static final String Soundapicateget = ""+url+"://"+domain_name+":"+port+"/api/audioCategory/create";
    public static final String Soundapicateid = ""+url+"://"+domain_name+":"+port+"/api/audioCategory/allCategoryData?id=";




    public static final String Soundapitrends = ""+url+"://"+domain_name+":"+port+"/api/sounds/trending?contentLangId=";
    public static final String Soundapicatdata = ""+url+"://"+domain_name+":"+port+"/api/sounds/category?contentLangId=";



    //add sounds

    public static final String Audiofile = ""+url+"://"+domain_name+":"+port+"/api/sounds/uploadAudio";
    public static final String Audiodata = ""+url+"://"+domain_name+":"+port+"/api/sounds/addData";

    ///stickers


    public static final String Stickers = "" + url + "://" + domain_name + ":" + port + "/api/stickerCategory/";
    public static final String Stickerslist = "" + url + "://" + domain_name + ":" + port + "/api/stickerCategory/CategoryData?id=";

}

/*
    static String domain_name = "13.235.9.168";  //test
    static String port = "4000"; //test
    static String url="http"; //test 13.235.9.168:4000/
    http://13.235.9.168:4000/api/home
*/



/*
    static String domain_name = "api";  //live
    static String port = "vasmash.com"; //live
    static String url="https"; //live

    //register
    public static final String sendotp_url = ""+url+"://"+domain_name+"."+port+"/api/users";
    public static final String submitotp_url = ""+url+"://"+domain_name+"."+port+"/api/users/verifyOtp";

    public static final String Sendotp = ""+url+"://"+domain_name+"."+port+"/api/users";
    public static final String Submitotp = ""+url+"://"+domain_name+"."+port+"/api/users/verifyOtp";
    public static final String Languagesapi = ""+url+"://"+domain_name+"."+port+"/api/contentlang";
    public static final String Soundapi = ""+url+"://"+domain_name+"."+port+"/api/sounds";
    public static final String Catageriesapi = ""+url+"://"+domain_name+"."+port+"/api/category";
    public static final String Countrysapi = ""+url+"://"+domain_name+"."+port+"/api/users/countries";
    public static final String registerprofileapi = ""+url+"://"+domain_name+"."+port+"/api/users/signup";

    public static final String preference_url = ""+url+"://"+domain_name+"."+port+"/api/users/preferences";
    public static final String updateprference_url = ""+url+"://"+domain_name+"."+port+"/api/users/updatePreferences";


    //firebase
    public static final String fcmtoken_url = ""+url+"://"+domain_name+"."+port+"/api/fcmToken/post";
    public static final String dynamiclink_url = ""+url+"://"+domain_name+"."+port+"/api/home/getPost?id=";


    //login
    public static final String login_pwdurl = ""+url+"://"+domain_name+"."+port+"/api/users/signin";
    public static final String loginwithotp_url = ""+url+"://"+domain_name+"."+port+"/api/users/signinWithOtp";
    public static final String verifyotp_url = ""+url+"://"+domain_name+"."+port+"/api/users/verifyLoginOtp";
    public static final String resendotp_url = ""+url+"://"+domain_name+"."+port+"/api/users/resendLoginOtp";
    public static final String forgetpwd_url = ""+url+"://"+domain_name+"."+port+"/api/users/forgotPassword";
    public static final String forgetotp_url = ""+url+"://"+domain_name+"."+port+"/api/users/forgotOtpVerify";
    public static final String activeuser_url = ""+url+"://"+domain_name+"."+port+"/api/users/activateUser";

    public static final String loginwithga_url = ""+url+"://"+domain_name+"."+port+"/api/users/loginWithGa";
    public static final String checkstatus_url = ""+url+"://"+domain_name+"."+port+"/api/users/checkStatus";

    public static final String regupload = ""+url+"://"+domain_name+"."+port+"/api/users/uploadProfilePic?userId=";


    //profile
    public static final String profiledetails_url = ""+url+"://"+domain_name+"."+port+"/api/users/current";
    public static final String updateprofile_url = ""+url+"://"+domain_name+"."+port+"/api/users/updateProfile";
    public static final String updateprofilepic_url = ""+url+"://"+domain_name+"."+port+"/api/users/updateProfilePic";
    public static final String usersliked_url = ""+url+"://"+domain_name+"."+port+"/api/likes/userLikedVideos";

    public static final String getprofile_url = "" + url + "://" + domain_name + "." + port + "/api/users/getProfile";
    public static final String usercreated_url = "" + url + "://" + domain_name + "." + port + "/api/v2/home/userCreatedPosts";
    public static final String userfollowing_url = "" + url + "://" + domain_name + "." + port + "/api/v2/follow/getFollowing";
    public static final String userfollower_url = "" + url + "://" + domain_name + "." + port + "/api/v2/follow/getFollowers";


    //other profile
    public static final String otherprofile_url = ""+url+"://"+domain_name+"."+port+"/api/users/othersProfile?id=";
    public static final String otherprofileuser_url = ""+url+"://"+domain_name+"."+port+"/api/home/otherUserCreatedPosts?userId=";
    public static final String follow_url = ""+url+"://"+domain_name+"."+port+"/api/follow/";
    public static final String unfollow_url = ""+url+"://"+domain_name+"."+port+"/api/follow/unFollow";

    public static final String other_url = "" + url + "://" + domain_name + "." + port + "/api/users/other?id=";
    public static final String otherollowing_url = "" + url + "://" + domain_name + "." + port + "/api/v2/follow/getOtherFollowing?id=";
    public static final String otherfollower_url = "" + url + "://" + domain_name + "." + port + "/api/v2/follow/getOtherFollowers";


    //wallet
    public static final String wallet_url = ""+url+"://"+domain_name+"."+port+"/api/wallets/";
    public static final String getwallwtbal_url = ""+url+"://"+domain_name+"."+port+"/api/wallets/walletBalance";
    public static final String p2ptrans_url = ""+url+"://"+domain_name+"."+port+"/api/wallets/p2pTransactions";
    public static final String getalltransactions_url = ""+url+"://"+domain_name+"."+port+"/api/wallets/allTransactions";

    //va Content
    public static final String addupload_url = ""+url+"://"+domain_name+"."+port+"/api/storePosters/uploadPoster";
    public static final String vaadddata_url = ""+url+"://"+domain_name+"."+port+"/api/storePosters/addData";
    public static final String vastoredata_url = ""+url+"://"+domain_name+"."+port+"/api/storePosters";


    //homefragmnt
    public static final String homeapi_url = ""+url+"://"+domain_name+"."+port+"/api/home";
        public static String homepagination_url = ""+url+"://"+domain_name+"."+port+"/api/home/posts?limit=10&skip=";
    public static final String earningpoints_url = ""+url+"://"+domain_name+"."+port+"/api/wallets/walletBalance";
    public static final String earningpost_url = ""+url+"://"+domain_name+"."+port+"/api/wallets/updateSmashBalance";
    public static final String claimsmash_url = ""+url+"://"+domain_name+"."+port+"/api/wallets/claimSmashCoin";
    public static final String getamount_url = ""+url+"://"+domain_name+"."+port+"/api/earnCoins/getAmountPerVideo";
    public static final String userreport_url = ""+url+"://"+domain_name+"."+port+"/api/report/user";
    public static final String postreport_url = ""+url+"://"+domain_name+"."+port+"/api/report/post";
    public static final String block_url = ""+url+"://"+domain_name+"."+port+"/api/follow/block";
    public static final String unblock_url = ""+url+"://"+domain_name+"."+port+"/api/follow/unBlock";
        public static final String hashtags_url = ""+url+"://"+domain_name+"."+port+"/api/home/tags?tag=";
        public static final String deletepost_url = ""+url+"://"+domain_name+"."+port+"/api/home/delete";
        public static final String visibility_url = ""+url+"://"+domain_name+"."+port+"/api/home/update";
        public static final String originalsound_url = "" + url + "://" + domain_name + "." + port + "/api/sounds/audio?id=";


    //comment
    public static final String comment_url = ""+url+"://"+domain_name+"."+port+"/api/comments/?postId=";
    public static final String posteomment_url = ""+url+"://"+domain_name+"."+port+"/api/comments/addComment";
    public static final String likescomment_url = ""+url+"://"+domain_name+"."+port+"/api/likes/likeComments";
    public static final String unlikecomment_url = ""+url+"://"+domain_name+"."+port+"/api/likes/removeCommentsLike";
    public static final String getfollowing_url = ""+url+"://"+domain_name+"."+port+"/api/follow/getFollowing";


    public static final String deletcomment_url = "" + url + "://" + domain_name + "." + port + "/api/comments/delete";
    public static final String reportcomment_url = "" + url + "://" + domain_name + "." + port + "/api/report/comment";
    public static final String replycomment_url = "" + url + "://" + domain_name + "." + port + "/api/v2/comments/replyComment";


    //likes
    public static final String getlikes_url = ""+url+"://"+domain_name+"."+port+"/api/likes/?postId=";
    public static final String likesend_url = ""+url+"://"+domain_name+"."+port+"/api/likes/like";
    public static final String unlike_url = ""+url+"://"+domain_name+"."+port+"/api/likes/removeLike";

    //share
    public static final String share_url = ""+url+"://"+domain_name+"."+port+"/api/home/sharePost?postId=";

    //search

    public static final String search_url = ""+url+"://"+domain_name+"."+port+"/api/home/search?term=";
    public static final String searchdata_url = ""+url+"://"+domain_name+"."+port+"/api/home/searchTerms?term=";

    public static final String usersearch_url = "" + url + "://" + domain_name + "." + port + "/api/v2/home/userSearch?term=";
    public static final String usersound_url = "" + url + "://" + domain_name + "." + port + "/api/v2/home/searchOnSounds?term=";
    public static final String hashtagsearch_url = "" + url + "://" + domain_name + "." + port + "/api/v2/home/searchOnTags?term=";
    public static final String searchall_url = "" + url + "://" + domain_name + "." + port + "/api/v2/home/searchAll?term=";


    //content

    public static final String add_image_content = ""+url+"://"+domain_name+"."+port+"/api/home/uploadImage";
    public static final String add_video_content = ""+url+"://"+domain_name+"."+port+"/api/home/uploadVideo";
    public static final String add_description_content = ""+url+"://"+domain_name+"."+port+"/api/home/updatePost";


    //setting notifications
    public static final String getnotification_url = ""+url+"://"+domain_name+"."+port+"/api/users/notificationPreferences";

        public static final String Soundapicate = ""+url+"://"+domain_name+"."+port+"/api/audioCategory";
        public static final String Soundapicateid = ""+url+"://"+domain_name+"."+port+"/api/audioCategory/allCategoryData?id=";


        public static final String Stickers = ""+url+"://"+domain_name+"."+port+"/api/stickerCategory/";
        public static final String Stickerslist = ""+url+"://"+domain_name+"."+port+"/api/stickerCategory/CategoryData?id=";

    public static final String Soundapicateget = ""+url+"://"+domain_name+"."+port+"/api/audioCategory/create";


    //add sounds
    public static final String Audiofile = ""+url+"://"+domain_name+"."+port+"/api/sounds/uploadAudio";
    public static final String Audiodata = ""+url+"://"+domain_name+"."+port+"/api/sounds/addData";


}
*/

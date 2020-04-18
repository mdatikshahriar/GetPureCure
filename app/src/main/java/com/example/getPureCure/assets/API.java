package com.example.getPureCure.assets;

public class API {

    private static String main_link = "https://getpurecure.herokuapp.com";

    //User APIs
    //GET
    private static String get_user_profile = main_link + "/user/get?id=";
    private static String get_user_profile_by_type = main_link + "/user/get?type=";
    private static String verify_user_session = main_link + "/user/session?";
    //POST
    private static String sign_up = main_link + "/user/signup";
    private static String sign_in = main_link + "/user/signin";
    private static String sign_out = main_link + "/user/signout";
    private static String update_password = main_link + "/user/updatepassword";
    private static String update_profile = main_link + "/user/updateprofile";

    //Patient APIs
    //GET
    private static String get_patient_history = main_link + "/patient/gethistory?user_id=";
    //POST
    private static String add_patient_history = main_link + "/patient/addhistory";
    //DELETE
    private static String delete_patient_history = main_link + "/patient/deletehistory";

    //Doctor APIs
    //GET
    private static String get_doctor_categories = main_link + "/doctor/categories";
    private static String get_doctors_by_category = main_link + "/doctor/getbycategory?category=";
    private static String get_popular_doctors = main_link + "/doctor/getbytype?type=popular";
    private static String get_suggested_doctors = main_link + "/doctor/getsuggested?user_id=";
    private static String get_specific_doctors = main_link + "/doctor/getspecific?id=";
    //POST
    private static String add_doctor_category = main_link + "/doctor/addcategory";
    private static String add_doctor_info = main_link + "/doctor/add";
    private static String edit_doctor_info = main_link + "/doctor/edit";
    //DELETE
    private static String delete_doctor_category = main_link + "/doctor/deleteCategory";
    private static String delete_doctor_info = main_link + "/doctor/delete";

    //Nutritionist APIs
    //GET
    private static String get_nutritionist_categories = main_link + "/nutritionist/categories";
    private static String get_nutritionists_by_category = main_link + "/nutritionist/getbycategory?category=";
    private static String get_popular_nutritionists = main_link + "/nutritionist/getbytype?type=popular";
    private static String get_suggested_nutritionists = main_link + "/nutritionist/getsuggested?user_id=";
    private static String get_specific_nutritionist = main_link + "/nutritionist/getspecific?id=";
    //POST
    private static String add_nutritionist_category = main_link + "/nutritionist/addcategory";
    private static String add_nutritionist_info = main_link + "/nutritionist/add";
    private static String edit_nutritionist_info = main_link + "/nutritionist/edit";
    //DELETE
    private static String delete_nutritionist_category = main_link + "/nutritionist/deleteCategory";
    private static String delete_nutritionist_info = main_link + "/nutritionist/delete";

    //Student APIs
    //GET
    private static String get_all_student = main_link + "/student/all";
    private static String get_specific_student = main_link + "/student/get?user_id=";
    //POST
    private static String add_student_info = main_link + "/student/add";
    private static String edit_student_info = main_link + "/student/edit";
    //DELETE
    private static String delete_student_info = main_link + "/student/delete";

    //Blog APIs
    //GET
    private static String get_blog_categories = main_link + "/blog/categories";
    private static String get_blog_by_category = main_link + "/blog/getbycategory?category=";
    private static String get_popular_blog = main_link + "/blog/getbytype?type=popular";
    private static String get_recent_blog = main_link + "/blog/getbytype?type=recent";
    private static String get_suggested_blog = main_link + "/blog/getsuggested?user_id=";
    private static String get_specific_blog = main_link + "/blog/getspecific?id=";
    //POST
    private static String add_blog_category = main_link + "/blog/addcategory";
    private static String add_blog = main_link + "/blog/add";
    private static String edit_blog = main_link + "/blog/edit";
    private static String like_blog = main_link + "/blog/like";
    private static String unlike_blog = main_link + "/blog/unlike";
    private static String add_comment = main_link + "/blog/comment";
    private static String add_reply = main_link + "/blog/reply";
    //DELETE
    private static String delete_blog_category = main_link + "/blog/deleteCategory";
    private static String delete_blog = main_link + "/blog/delete";

    //Hospital APIs
    //GET
    private static String get_hospital_categories = main_link + "/hospital/categories";
    private static String get_hospitals_by_category = main_link + "/hospital/getbycategory?category=";
    private static String get_popular_hospitals = main_link + "/hospital/getbytype?type=popular";
    private static String get_suggested_hospitals = main_link + "/hospital/getsuggested?user_id=";
    private static String get_specific_hospital = main_link + "/hospital/getspecific?id=";
    //POST
    private static String add_hospital_category = main_link + "/hospital/addcategory";
    private static String add_hospital = main_link + "/hospital/add";
    private static String edit_hospital = main_link + "/hospital/edit";
    private static String add_rating = main_link + "/hospital/addrating";
    //DELETE
    private static String delete_hospital_category = main_link + "/hospital/deleteCategory";
    private static String delete_hospital = main_link + "/hospital/delete";

    //Video APIs
    //GET
    private static String get_video_categories = main_link + "/video/categories";
    private static String get_videos_by_category = main_link + "/video/getbycategory?category=";
    private static String get_recent_videos = main_link + "/video/getbytype?type=recent";
    private static String get_popular_videos = main_link + "/video/getbytype?type=popular";
    private static String get_suggested_videos = main_link + "/video/getsuggested?user_id=";
    private static String get_specific_video = main_link + "/video/getspecific?id=";
    //POST
    private static String add_video_category = main_link + "/video/addcategory";
    private static String add_video = main_link + "/video/add";
    private static String edit_video = main_link + "/video/edit";
    //DELETE
    private static String delete_video_category = main_link + "/video/deleteCategory";
    private static String delete_video = main_link + "/video/delete";

    //Book APIs
    //GET
    private static String get_book_categories = main_link + "/book/categories";
    private static String get_books_by_category = main_link + "/book/getbycategory?category=";
    private static String get_recent_books = main_link + "/book/getbytype?type=recent";
    private static String get_popular_books = main_link + "/book/getbytype?type=popular";
    private static String get_suggested_books = main_link + "/book/getsuggested?user_id=";
    private static String get_specific_book = main_link + "/book/getspecific?id=";
    //POST
    private static String add_book_category = main_link + "/book/addcategory";
    private static String add_book = main_link + "/book/add";
    private static String edit_book = main_link + "/book/edit";
    //DELETE
    private static String delete_book_category = main_link + "/book/deleteCategory";
    private static String delete_book = main_link + "/book/delete";

    public API() {
    }

    public API(String main_link) {
        this.main_link = main_link;
    }

    public static String getMain_link() {
        return main_link;
    }

    public static String getGet_user_profile(String id) {
        return get_user_profile + id;
    }

    public static String getGet_user_profile_by_type(String type) {
        return get_user_profile_by_type + type;
    }

    public static String getVerify_user_session(String id, String token) {
        return verify_user_session + "id=" + id + "&token=" + token;
    }

    public static String getSign_up() {
        return sign_up;
    }

    public static String getSign_in() {
        return sign_in;
    }

    public static String getSign_out() {
        return sign_out;
    }

    public static String getUpdate_password() {
        return update_password;
    }

    public static String getUpdate_profile() {
        return update_profile;
    }

    public static String getGet_patient_history(String user_id) {
        return get_patient_history + user_id;
    }

    public static String getAdd_patient_history() {
        return add_patient_history;
    }

    public static String getDelete_patient_history() {
        return delete_patient_history;
    }

    public static String getGet_doctor_categories() {
        return get_doctor_categories;
    }

    public static String getGet_doctors_by_category(String category) {
        return get_doctors_by_category + category;
    }

    public static String getGet_popular_doctors() {
        return get_popular_doctors;
    }

    public static String getGet_suggested_doctors(String user_id) {
        return get_suggested_doctors + user_id;
    }

    public static String getGet_specific_doctors(String id) {
        return get_specific_doctors + id;
    }

    public static String getAdd_doctor_category() {
        return add_doctor_category;
    }

    public static String getAdd_doctor_info() {
        return add_doctor_info;
    }

    public static String getEdit_doctor_info() {
        return edit_doctor_info;
    }

    public static String getDelete_doctor_category() {
        return delete_doctor_category;
    }

    public static String getDelete_doctor_info() {
        return delete_doctor_info;
    }

    public static String getGet_nutritionist_categories() {
        return get_nutritionist_categories;
    }

    public static String getGet_nutritionists_by_category(String category) {
        return get_nutritionists_by_category + category;
    }

    public static String getGet_popular_nutritionists() {
        return get_popular_nutritionists;
    }

    public static String getGet_suggested_nutritionists(String user_id) {
        return get_suggested_nutritionists + user_id;
    }

    public static String getGet_specific_nutritionist(String id) {
        return get_specific_nutritionist + id;
    }

    public static String getAdd_nutritionist_category() {
        return add_nutritionist_category;
    }

    public static String getAdd_nutritionist_info() {
        return add_nutritionist_info;
    }

    public static String getEdit_nutritionist_info() {
        return edit_nutritionist_info;
    }

    public static String getDelete_nutritionist_category() {
        return delete_nutritionist_category;
    }

    public static String getDelete_nutritionist_info() {
        return delete_nutritionist_info;
    }

    public static String getGet_all_student() {
        return get_all_student;
    }

    public static String getGet_specific_student(String user_id) {
        return get_specific_student + user_id;
    }

    public static String getAdd_student_info() {
        return add_student_info;
    }

    public static String getEdit_student_info() {
        return edit_student_info;
    }

    public static String getDelete_student_info() {
        return delete_student_info;
    }

    public static String getGet_blog_categories() {
        return get_blog_categories;
    }

    public static String getGet_blog_by_category(String category) {
        return get_blog_by_category + category;
    }

    public static String getGet_popular_blog() {
        return get_popular_blog;
    }

    public static String getGet_recent_blog() {
        return get_recent_blog;
    }

    public static String getGet_suggested_blog(String user_id) {
        return get_suggested_blog + user_id;
    }

    public static String getGet_specific_blog(String id) {
        return get_specific_blog + id;
    }

    public static String getAdd_blog_category() {
        return add_blog_category;
    }

    public static String getAdd_blog() {
        return add_blog;
    }

    public static String getEdit_blog() {
        return edit_blog;
    }

    public static String getLike_blog() {
        return like_blog;
    }

    public static String getUnlike_blog() {
        return unlike_blog;
    }

    public static String getAdd_comment() {
        return add_comment;
    }

    public static String getAdd_reply() {
        return add_reply;
    }

    public static String getDelete_blog_category() {
        return delete_blog_category;
    }

    public static String getDelete_blog() {
        return delete_blog;
    }

    public static String getGet_hospital_categories() {
        return get_hospital_categories;
    }

    public static String getGet_hospitals_by_category(String category) {
        return get_hospitals_by_category + category;
    }

    public static String getGet_popular_hospitals() {
        return get_popular_hospitals;
    }

    public static String getGet_suggested_hospitals(String user_id) {
        return get_suggested_hospitals + user_id;
    }

    public static String getGet_specific_hospital(String id) {
        return get_specific_hospital + id;
    }

    public static String getAdd_hospital_category() {
        return add_hospital_category;
    }

    public static String getAdd_hospital() {
        return add_hospital;
    }

    public static String getEdit_hospital() {
        return edit_hospital;
    }

    public static String getAdd_rating() {
        return add_rating;
    }

    public static String getDelete_hospital_category() {
        return delete_hospital_category;
    }

    public static String getDelete_hospital() {
        return delete_hospital;
    }

    public static String getGet_video_categories() {
        return get_video_categories;
    }

    public static String getGet_videos_by_category(String category) {
        return get_videos_by_category + category;
    }

    public static String getGet_recent_videos() {
        return get_recent_videos;
    }

    public static String getGet_popular_videos() {
        return get_popular_videos;
    }

    public static String getGet_suggested_videos(String user_id) {
        return get_suggested_videos + user_id;
    }

    public static String getGet_specific_video(String id) {
        return get_specific_video + id;
    }

    public static String getAdd_video_category() {
        return add_video_category;
    }

    public static String getAdd_video() {
        return add_video;
    }

    public static String getEdit_video() {
        return edit_video;
    }

    public static String getDelete_video_category() {
        return delete_video_category;
    }

    public static String getDelete_video() {
        return delete_video;
    }

    public static String getGet_book_categories() {
        return get_book_categories;
    }

    public static String getAdd_book_category() {
        return add_book_category;
    }

    public static String getAdd_book() {
        return add_book;
    }

    public static String getEdit_book() {
        return edit_book;
    }

    public static String getGet_books_by_category(String category) {
        return get_books_by_category + category;
    }

    public static String getGet_recent_books() {
        return get_recent_books;
    }

    public static String getGet_popular_books() {
        return get_popular_books;
    }

    public static String getGet_suggested_books(String user_id) {
        return get_suggested_books + user_id;
    }

    public static String getGet_specific_book(String id) {
        return get_specific_book + id;
    }

    public static String getDelete_book_category() {
        return delete_book_category;
    }

    public static String getDelete_book() {
        return delete_book;
    }
}

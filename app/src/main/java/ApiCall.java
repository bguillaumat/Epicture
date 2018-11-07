class ApiCall {

    void uploadImg() {
        //https://api.imgur.com/3/image
    }

    void getUserImg(String username) {
        String url = "https://api.imgur.com/3/account/" + username + "/images";
    }

    void getRecentImg() {

    }

    void searchImg() {

    }

    void getFavorites() {
        //https://api.imgur.com/3/account/{{username}}/favorites/{{page}}/{{favoritesSort}}
    }

}

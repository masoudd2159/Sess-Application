package ir.ac.sku.www.sessapplication.models;

import java.util.List;

public class SFXWeeklyList {

    private Boolean ok;
    private Result result;
    private Description description;

    public Description getDescription() {
        return description;
    }

    public void setDescription(Description description) {
        this.description = description;
    }

    public Boolean isOk() {
        return ok;
    }

    public void setOk(Boolean ok) {
        this.ok = ok;
    }

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }


    public class Result {
        private List<Day0> day0 = null;
        private List<Day1> day1 = null;
        private List<Day2> day2 = null;
        private List<Day3> day3 = null;
        private List<Day4> day4 = null;
        private List<Day5> day5 = null;
        private List<Day6> day6 = null;

        private String credit;
        private String message;

        private List<Restaurant> restaurants = null;

        public List<Day0> getDay0() {
            return day0;
        }

        public void setDay0(List<Day0> day0) {
            this.day0 = day0;
        }

        public List<Day1> getDay1() {
            return day1;
        }

        public void setDay1(List<Day1> day1) {
            this.day1 = day1;
        }

        public List<Day2> getDay2() {
            return day2;
        }

        public void setDay2(List<Day2> day2) {
            this.day2 = day2;
        }

        public List<Day3> getDay3() {
            return day3;
        }

        public void setDay3(List<Day3> day3) {
            this.day3 = day3;
        }

        public List<Day4> getDay4() {
            return day4;
        }

        public void setDay4(List<Day4> day4) {
            this.day4 = day4;
        }

        public List<Day5> getDay5() {
            return day5;
        }

        public void setDay5(List<Day5> day5) {
            this.day5 = day5;
        }

        public List<Day6> getDay6() {
            return day6;
        }

        public void setDay6(List<Day6> day6) {
            this.day6 = day6;
        }

        public String getCredit() {
            return credit;
        }

        public void setCredit(String credit) {
            this.credit = credit;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public List<Restaurant> getRestaurants() {
            return restaurants;
        }

        public void setRestaurants(List<Restaurant> restaurants) {
            this.restaurants = restaurants;
        }


        public class Day0 {
            private String status;
            private String name;
            private String mealCode;
            private String restaurant;
            private String price;
            private String data;
            private String dateCode;
            private String dateText;

            public String getStatus() {
                return status;
            }

            public void setStatus(String status) {
                this.status = status;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getMealCode() {
                return mealCode;
            }

            public void setMealCode(String mealCode) {
                this.mealCode = mealCode;
            }

            public String getRestaurant() {
                return restaurant;
            }

            public void setRestaurant(String restaurant) {
                this.restaurant = restaurant;
            }

            public String getPrice() {
                return price;
            }

            public void setPrice(String price) {
                this.price = price;
            }

            public String getData() {
                return data;
            }

            public void setData(String data) {
                this.data = data;
            }

            public String getDateCode() {
                return dateCode;
            }

            public void setDateCode(String dateCode) {
                this.dateCode = dateCode;
            }

            public String getDateText() {
                return dateText;
            }

            public void setDateText(String dateText) {
                this.dateText = dateText;
            }
        }

        public class Day1 {
            private String status;
            private String name;
            private String mealCode;
            private String restaurant;
            private String price;
            private String data;
            private String dateCode;
            private String dateText;

            public String getStatus() {
                return status;
            }

            public void setStatus(String status) {
                this.status = status;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getMealCode() {
                return mealCode;
            }

            public void setMealCode(String mealCode) {
                this.mealCode = mealCode;
            }

            public String getRestaurant() {
                return restaurant;
            }

            public void setRestaurant(String restaurant) {
                this.restaurant = restaurant;
            }

            public String getPrice() {
                return price;
            }

            public void setPrice(String price) {
                this.price = price;
            }

            public String getData() {
                return data;
            }

            public void setData(String data) {
                this.data = data;
            }

            public String getDateCode() {
                return dateCode;
            }

            public void setDateCode(String dateCode) {
                this.dateCode = dateCode;
            }

            public String getDateText() {
                return dateText;
            }

            public void setDateText(String dateText) {
                this.dateText = dateText;
            }
        }

        public class Day2 {
            private String status;
            private String name;
            private String mealCode;
            private String restaurant;
            private String price;
            private String data;
            private String dateCode;
            private String dateText;

            public String getStatus() {
                return status;
            }

            public void setStatus(String status) {
                this.status = status;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getMealCode() {
                return mealCode;
            }

            public void setMealCode(String mealCode) {
                this.mealCode = mealCode;
            }

            public String getRestaurant() {
                return restaurant;
            }

            public void setRestaurant(String restaurant) {
                this.restaurant = restaurant;
            }

            public String getPrice() {
                return price;
            }

            public void setPrice(String price) {
                this.price = price;
            }

            public String getData() {
                return data;
            }

            public void setData(String data) {
                this.data = data;
            }

            public String getDateCode() {
                return dateCode;
            }

            public void setDateCode(String dateCode) {
                this.dateCode = dateCode;
            }

            public String getDateText() {
                return dateText;
            }

            public void setDateText(String dateText) {
                this.dateText = dateText;
            }
        }

        public class Day3 {
            private String status;
            private String name;
            private String mealCode;
            private String restaurant;
            private String price;
            private String data;
            private String dateCode;
            private String dateText;

            public String getStatus() {
                return status;
            }

            public void setStatus(String status) {
                this.status = status;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getMealCode() {
                return mealCode;
            }

            public void setMealCode(String mealCode) {
                this.mealCode = mealCode;
            }

            public String getRestaurant() {
                return restaurant;
            }

            public void setRestaurant(String restaurant) {
                this.restaurant = restaurant;
            }

            public String getPrice() {
                return price;
            }

            public void setPrice(String price) {
                this.price = price;
            }

            public String getData() {
                return data;
            }

            public void setData(String data) {
                this.data = data;
            }

            public String getDateCode() {
                return dateCode;
            }

            public void setDateCode(String dateCode) {
                this.dateCode = dateCode;
            }

            public String getDateText() {
                return dateText;
            }

            public void setDateText(String dateText) {
                this.dateText = dateText;
            }
        }

        public class Day4 {
            private String status;
            private String name;
            private String mealCode;
            private String restaurant;
            private String price;
            private String data;
            private String dateCode;
            private String dateText;

            public String getStatus() {
                return status;
            }

            public void setStatus(String status) {
                this.status = status;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getMealCode() {
                return mealCode;
            }

            public void setMealCode(String mealCode) {
                this.mealCode = mealCode;
            }

            public String getRestaurant() {
                return restaurant;
            }

            public void setRestaurant(String restaurant) {
                this.restaurant = restaurant;
            }

            public String getPrice() {
                return price;
            }

            public void setPrice(String price) {
                this.price = price;
            }

            public String getData() {
                return data;
            }

            public void setData(String data) {
                this.data = data;
            }

            public String getDateCode() {
                return dateCode;
            }

            public void setDateCode(String dateCode) {
                this.dateCode = dateCode;
            }

            public String getDateText() {
                return dateText;
            }

            public void setDateText(String dateText) {
                this.dateText = dateText;
            }
        }

        public class Day5 {
            private String status;
            private String name;
            private String mealCode;
            private String restaurant;
            private String price;
            private String data;
            private String dateCode;
            private String dateText;

            public String getStatus() {
                return status;
            }

            public void setStatus(String status) {
                this.status = status;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getMealCode() {
                return mealCode;
            }

            public void setMealCode(String mealCode) {
                this.mealCode = mealCode;
            }

            public String getRestaurant() {
                return restaurant;
            }

            public void setRestaurant(String restaurant) {
                this.restaurant = restaurant;
            }

            public String getPrice() {
                return price;
            }

            public void setPrice(String price) {
                this.price = price;
            }

            public String getData() {
                return data;
            }

            public void setData(String data) {
                this.data = data;
            }

            public String getDateCode() {
                return dateCode;
            }

            public void setDateCode(String dateCode) {
                this.dateCode = dateCode;
            }

            public String getDateText() {
                return dateText;
            }

            public void setDateText(String dateText) {
                this.dateText = dateText;
            }
        }

        public class Day6 {
            private String status;
            private String name;
            private String mealCode;
            private String restaurant;
            private String price;
            private String data;
            private String dateCode;
            private String dateText;

            public String getStatus() {
                return status;
            }

            public void setStatus(String status) {
                this.status = status;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getMealCode() {
                return mealCode;
            }

            public void setMealCode(String mealCode) {
                this.mealCode = mealCode;
            }

            public String getRestaurant() {
                return restaurant;
            }

            public void setRestaurant(String restaurant) {
                this.restaurant = restaurant;
            }

            public String getPrice() {
                return price;
            }

            public void setPrice(String price) {
                this.price = price;
            }

            public String getData() {
                return data;
            }

            public void setData(String data) {
                this.data = data;
            }

            public String getDateCode() {
                return dateCode;
            }

            public void setDateCode(String dateCode) {
                this.dateCode = dateCode;
            }

            public String getDateText() {
                return dateText;
            }

            public void setDateText(String dateText) {
                this.dateText = dateText;
            }
        }

        public class Restaurant {

            private String name;
            private String code;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getCode() {
                return code;
            }

            public void setCode(String code) {
                this.code = code;
            }
        }
    }

    public class Description {
        private String errorText;
        private String errorCode;

        public String getErrorText() {
            return errorText;
        }

        public void setErrorText(String errorText) {
            this.errorText = errorText;
        }

        public String getErrorCode() {
            return errorCode;
        }

        public void setErrorCode(String errorCode) {
            this.errorCode = errorCode;
        }
    }
}

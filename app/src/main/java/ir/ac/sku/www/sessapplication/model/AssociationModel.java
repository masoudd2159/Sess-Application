package ir.ac.sku.www.sessapplication.model;

import java.util.List;

public class AssociationModel {
    private String section;
    private List<Data> data = null;

    // flag when item swiped
    public boolean swiped = false;

    public boolean expanded = false;
    public boolean parent = false;

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public List<Data> getData() {
        return data;
    }

    public void setData(List<Data> data) {
        this.data = data;
    }

    public boolean isSwiped() {
        return swiped;
    }

    public void setSwiped(boolean swiped) {
        this.swiped = swiped;
    }

    public boolean isExpanded() {
        return expanded;
    }

    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }

    public boolean isParent() {
        return parent;
    }

    public void setParent(boolean parent) {
        this.parent = parent;
    }

    public class Data {
        private String faName;
        private String enName;
        private String description;
        private String logo;
        private String secretary;
        private String telegram;
        private String instagram;
        private String twitter;
        private String email;
        private String phone;
        private String website;
        private String establishedYear;
        private List<Admin> admin = null;

        public String getFaName() {
            return faName;
        }

        public void setFaName(String faName) {
            this.faName = faName;
        }

        public String getEnName() {
            return enName;
        }

        public void setEnName(String enName) {
            this.enName = enName;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getLogo() {
            return logo;
        }

        public void setLogo(String logo) {
            this.logo = logo;
        }

        public String getSecretary() {
            return secretary;
        }

        public void setSecretary(String secretary) {
            this.secretary = secretary;
        }

        public String getTelegram() {
            return telegram;
        }

        public void setTelegram(String telegram) {
            this.telegram = telegram;
        }

        public String getInstagram() {
            return instagram;
        }

        public void setInstagram(String instagram) {
            this.instagram = instagram;
        }

        public String getTwitter() {
            return twitter;
        }

        public void setTwitter(String twitter) {
            this.twitter = twitter;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getWebsite() {
            return website;
        }

        public void setWebsite(String website) {
            this.website = website;
        }

        public String getEstablishedYear() {
            return establishedYear;
        }

        public void setEstablishedYear(String establishedYear) {
            this.establishedYear = establishedYear;
        }

        public List<Admin> getAdmin() {
            return admin;
        }

        public void setAdmin(List<Admin> admin) {
            this.admin = admin;
        }

        public class Admin {
            private String name;
            private String telegramId;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getTelegramId() {
                return telegramId;
            }

            public void setTelegramId(String telegramId) {
                this.telegramId = telegramId;
            }
        }
    }
}

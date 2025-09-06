package app.utils;

import app.dao.ParcelDao;

public class Main {
    public static void main(String[]args){
        ParcelDao parcelDao = ParcelDao.getEntityManagerFactory();



        parcelDao.close();
    }
}

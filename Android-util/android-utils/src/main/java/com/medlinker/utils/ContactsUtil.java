package com.medlinker.utils;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.support.v4.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

/**
 * @author <a href="mailto:tql@medlinker.net">tqlmorepassion</a>
 * @version 1.0
 * @description 获取手机联系人
 * @time 2016/5/10 11:58
 */
public class ContactsUtil {

    /**
     * 读取手机联系人列表
     *
     * @param context
     * @return
     */
    public static List<ContactsEntity> readContactsList(Context context) {
        List<ContactsEntity> contactsEntitieList = new ArrayList<>();
        Cursor cursor = context.getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
        int contactIdIndex = 0;
        int nameIndex = 0;
        if (cursor.getCount() > 0) {
            contactIdIndex = cursor.getColumnIndex(ContactsContract.Contacts._ID);
            nameIndex = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
        }
        ContactsEntity contactsEntity = null;
        Cursor phoneCursor = null;
        while (cursor.moveToNext()) {
            String contactId = cursor.getString(contactIdIndex);
            String name = cursor.getString(nameIndex);
            // 查找联系人的电话信息
            phoneCursor = context.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=" + contactId, null, null);
            int phoneIndex = 0;
            if (phoneCursor.getCount() > 0) {
                phoneIndex = phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                // 用户可能存在多个电话号码
                while (phoneCursor.moveToNext()) {
                    String phoneNumber = phoneCursor.getString(phoneIndex);
                    contactsEntity = new ContactsEntity();
                    contactsEntity.setId(contactId);
                    contactsEntity.setName(name);
                    contactsEntity.setPhoneNumber(phoneNumber);
                    contactsEntitieList.add(contactsEntity);
                }
            } else {
                // 用户只存了名字
                contactsEntity = new ContactsEntity();
                contactsEntity.setId(contactId);
                contactsEntity.setName(name);
                contactsEntity.setPhoneNumber("");
                contactsEntitieList.add(contactsEntity);
            }
        }
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
        if (phoneCursor != null && !phoneCursor.isClosed()) {
            phoneCursor.close();
        }
        return contactsEntitieList;
    }

    /**
     * 读取手机联系人电话号码列表
     *
     * @param context
     * @return
     */
    public static List<String> readContactsMobileList(Context context) {
        if (!checkContactPermission(context)) {
            return null;//如果没有取得获取通讯录的权限，则不获取（6.0以上崩溃）
        }

        List<String> phoneNumberList = new ArrayList<>();
        ContentResolver resolver = context.getContentResolver();
        Cursor cursor = resolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                ContactsContract.CommonDataKinds.Phone.TYPE + "=" + ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE, null, null);
        int phoneIndex = 0;
        if (cursor != null && cursor.getCount() > 0) {
            phoneIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
            while (cursor.moveToNext()) {
                String phoneNumber = cursor.getString(phoneIndex);

                phoneNumberList.add(phoneNumber);
            }
        }
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
        return phoneNumberList;
    }

    public static class ContactsEntity {

        private String id;

        private String name;

        private String phoneNumber;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPhoneNumber() {
            return phoneNumber;
        }

        public void setPhoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
        }

        @Override
        public String toString() {
            return "ContactsEntity{" +
                    "id='" + id + '\'' +
                    ", name='" + name + '\'' +
                    ", phoneNumber='" + phoneNumber + '\'' +
                    '}';
        }
    }

    /**
     * * 检查是否获得读取联系人的权限
     * 还需要在指定的activity里面重写onRequestPermissionsResult方法，处理授权以后的操作
     *
     * @return
     */
    private static Boolean checkContactPermission(Context context) {
        return ContextCompat.checkSelfPermission(context, Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED;
    }
}

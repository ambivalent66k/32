package org.example.storagesystem.utils;

import org.example.storagesystem.entity.StorageObject;
import org.example.storagesystem.exception.ErrorCode;
import org.example.storagesystem.exception.custom.BusinessException;

import java.lang.reflect.Field;

public class AttributeUtils {
    public static Object getFieldValue(StorageObject obj, String fieldName) {
        try {
            Field field = StorageObject.class.getDeclaredField(fieldName);
            field.setAccessible(true);
            return field.get(obj);
        } catch (Exception e) {
            throw new BusinessException(ErrorCode.UNKNOWN_ATTRIBUTE, fieldName);
        }
    }
}

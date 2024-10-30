package com.example.WebBanSach.Validtion;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({TYPE, FIELD}) //Áp dụng cho các loại dữ liệu (class
@Retention(RUNTIME) //Cho phép được sử dụng runtime để thực
@Constraint(validatedBy = ValidUsernameValidator.class)
public @interface ValidUsername {
    String message() default "Tên đăng nhập không hợp Lệ";
    Class <? >[] groups() default {};
    Class <? extends Payload>[] payload() default {};
}

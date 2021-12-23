package com.krishiv.myislam.utils;

import com.krishiv.myislam.dto.RegisterModel;

public class GlobalVariable {
    static RegisterModel registerModel;

    public static RegisterModel getRegistrationModel(){
        if (registerModel == null)
            registerModel = new RegisterModel();
        return registerModel;
    }
}

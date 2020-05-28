package com.example.kaush;

import java.util.HashMap;
import java.util.Map;

public class UserData
{
    private String password;
    private String name;

    public UserData(String name, String password)
    {
        this.password = password;
        this.name = name;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String email)
    {
        this.name = email;
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("email", name);
        result.put("password", password);


        return result;
    }



}

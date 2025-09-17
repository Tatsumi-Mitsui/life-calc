package com.example.demo.core.context;

import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpSession;

@Service
public class UserContextService {
    
    /*
     * ログインしていないユーザーに対して、セッション単位で一意なIDを割り当てる。
     * 既に割り当て済みなら再利用する。
     */

    public Long resolveUserId(HttpSession session) {

        Object attr = session.getAttribute("userId");
        if (attr instanceof Long uid && uid != 0L){
            return uid;
        }
        // 未ログイン時は 0L を返す
        return 0L;
    }
}
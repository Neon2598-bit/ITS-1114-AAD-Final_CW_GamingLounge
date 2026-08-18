package edu.ijse.gamingLounge.constant;

public class ResponseCode {
    public static final int SUCCESS = 200;       // GET / PUT / DELETE succeeded
    public static final int CREATED = 201;        // POST succeeded (new resource created)
    public static final int BAD_REQUEST = 400;    // validation failed / invalid input
    public static final int UNAUTHORIZED = 401;   // missing/invalid/expired JWT token
    public static final int FORBIDDEN = 403;       // valid token, but wrong role (e.g. USER hitting ADMIN endpoint)
    public static final int NOT_FOUND = 404;      // requested record does not exist
    public static final int CONFLICT = 409;        // duplicate data — e.g. branch name/contact already exists
    public static final int SERVER_ERROR = 500;   // unexpected error
}

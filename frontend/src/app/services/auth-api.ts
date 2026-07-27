import { Injectable } from "@angular/core";
import { environment } from "../../environments/environment";
import { HttpClient } from "@angular/common/http";
import { Observable } from "rxjs";
import { LoginRequest, RegisterRequest, UserSessionResponse } from "../models/auth.models";

@Injectable({ providedIn: 'root' })
export class AuthApi {
    private readonly baseUrl = environment.apiBaseUrl;

    constructor(private http: HttpClient){}

    me(): Observable<UserSessionResponse> {
        return this.http.get<UserSessionResponse>(`${this.baseUrl}/auth/me`);
    }

    register(user: RegisterRequest): Observable<UserSessionResponse>{
        return this.http.post<UserSessionResponse>(`${this.baseUrl}/auth/register`, user);
    }

    login(user: LoginRequest): Observable<UserSessionResponse>{
        return this.http.post<UserSessionResponse>(`${this.baseUrl}/auth/login`, user);
    }
}
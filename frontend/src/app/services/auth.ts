import { Injectable } from "@angular/core";
import { catchError, map, Observable, of, tap } from "rxjs";
import { AuthApi } from './auth-api';
import { LoginRequest, RegisterRequest, UserRole, UserSessionResponse } from "../models/auth.models";

@Injectable({ providedIn: 'root' })
export class Auth {
    private currentUser: UserSessionResponse | null = null;

    constructor(private authApi: AuthApi) {}

    getCurrentUser(): UserSessionResponse | null {
        return this.currentUser;
    }

    getRole(): UserRole | null {
        return this.getCurrentUser()?.role ?? null;
    }

    isAdmin(): boolean {
        return this.getRole() === 'ADMIN';
    }

    isUser(): boolean {
        return this.getRole() === 'USER';
    }

    loadCurrentUser(): Observable<UserSessionResponse | null> {
        return this.authApi.me().pipe(
            tap((user) => {
                this.currentUser = user;
            }),
            catchError(() => {
                this.clearSession();
                return of(null);
            })
        );
    }

    ensureCurrentUser(): Observable<UserSessionResponse | null> {
        if (this.currentUser) {
            return of(this.currentUser);
        }

        return this.loadCurrentUser();
    }

    isAuthenticated(): Observable<boolean> {
        return this.ensureCurrentUser().pipe(
            map((user) => user !== null)
        );
    }

    clearSession(): void {
        this.currentUser = null;
    }

    register(request: RegisterRequest): Observable<UserSessionResponse> {
        return this.authApi.register(request).pipe(
            tap((response) => {
                this.currentUser = response;
            })
        );
    }

    login(request: LoginRequest): Observable<UserSessionResponse> {
        return this.authApi.login(request).pipe(
            tap((response) => {
                this.currentUser = response;
            })
        );
    }
}
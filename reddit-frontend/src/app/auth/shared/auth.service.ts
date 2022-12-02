import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { LocalStorageService } from 'ngx-webstorage';
import { SignupRequestPayload } from '../signup/signup-request.payload';
import { HttpClient } from '@angular/common/http';
import { LoginRequestPayload } from '../signup/login-request.payload';
import { LoginResponse } from '../signup/login-response.payload';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  constructor(
    private http: HttpClient,
    private localStorage: LocalStorageService,
  ) {}

  signup(signupRequestPayload: SignupRequestPayload): Observable<any> {
    return this.http.post(
      'http://localhost:8080/api/auth/signup',
      signupRequestPayload,
      { responseType: 'text' },
    );
  }

  login(loginRequestPayload: LoginRequestPayload): Observable<boolean> {
    return this.http
      .post<LoginResponse>(
        'http://localhost:8080/api/auth/login',
        loginRequestPayload,
      )
      .pipe(
        map(data => {
          this.localStorage.store(
            'authenticationToken',
            data.authenticationToken,
          );
          this.localStorage.store('username', data.username);
          this.localStorage.store('refreshToken', data.refreshToken);
          this.localStorage.store('expiresAt', data.expiresAt);

          return true;
        }),
      );
  }
}

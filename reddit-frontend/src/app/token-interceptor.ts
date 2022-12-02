import { Injectable } from '@angular/core';
import {
  HttpInterceptor,
  HttpRequest,
  HttpHandler,
  HttpEvent,
  HttpErrorResponse,
} from '@angular/common/http';
import { Observable, throwError, BehaviorSubject } from 'rxjs';
import { catchError, switchMap } from 'rxjs/operators';
import { LoginResponse } from './auth/signup/login-response.payload';
import { AuthService } from './auth/shared/auth.service';

@Injectable({
  providedIn: 'root',
})
export class TokenInterceptor implements HttpInterceptor {
  isTokenRefreshing = false;
  refreshTokenSubject: BehaviorSubject<any> = new BehaviorSubject(null);

  constructor(public authService: AuthService) {}

  intercept(
    req: HttpRequest<any>,
    next: HttpHandler,
  ): Observable<HttpEvent<any>> {
    // Grab existing token and put it in the (request) header
    if (this.authService.getJwtToken()) {
      this.addToken(req, this.authService.getJwtToken());
    }

    // If there were a 403 error code, it means that the JWT token are
    // already expired. Now we need to handle it (see notes down below)
    return next.handle(req).pipe(
      catchError(error => {
        if (error instanceof HttpErrorResponse && error.status === 403) {
          return this.handleAuthErrors(req, next);
        } else {
          return throwError(error);
        }
      }),
    );
  }

  private handleAuthErrors(
    req: HttpRequest<any>,
    next: HttpHandler,
  ): Observable<HttpEvent<any>> {
    // We weren't going to request a new JWT token every time we called
    // API or visited the site, as it would need to reach to the point
    // of a 403 error to even get here.
    if (!this.isTokenRefreshing) {
      this.isTokenRefreshing = true;
      this.refreshTokenSubject.next(null);

      // Call relevant API path to using the username and the refreshToken
      // that was saved at the first time to get a new JWT token.
      return this.authService.refreshToken().pipe(
        switchMap((refreshTokenResponse: LoginResponse) => {
          this.isTokenRefreshing = false;
          this.refreshTokenSubject.next(
            refreshTokenResponse.authenticationToken,
          );

          // Grab the JWT token from the response
          return next.handle(
            this.addToken(req, refreshTokenResponse.authenticationToken),
          );
        }),
      );
    }
  }

  private addToken(req: HttpRequest<any>, jwtToken: string) {
    return req.clone({
      headers: req.headers.set('Authorization', 'Bearer ' + jwtToken),
    });
  }
}

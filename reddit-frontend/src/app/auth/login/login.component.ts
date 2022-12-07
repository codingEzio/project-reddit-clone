import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';

import { LoginRequestPayload } from '../signup/login-request.payload';
import { AuthService } from '../shared/auth.service';
import { throwError } from 'rxjs';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css'],
})
export class LoginComponent implements OnInit {
  loginForm: FormGroup;
  loginRequestPayload: LoginRequestPayload;
  registerSuccessMessage: string;
  isError: boolean;

  constructor(
    private authService: AuthService,
    private activatedRoute: ActivatedRoute,
    private router: Router,
    private toastr: ToastrService,
  ) {
    this.loginRequestPayload = {
      username: '',
      password: '',
    };
  }

  ngOnInit(): void {
    this.loginForm = new FormGroup({
      username: new FormControl('', Validators.required),
      password: new FormControl('', Validators.required),
    });

    this.activatedRoute.queryParams.subscribe(params => {
      if (params.registered !== undefined && params.registered === 'true') {
        this.toastr.success('Signup successful 😊');
        this.registerSuccessMessage =
          'Plase check your inbox for account activation email 😇';
      }
    });
  }

  login() {
    this.loginRequestPayload.username = this.loginForm.get('username').value;
    this.loginRequestPayload.password = this.loginForm.get('password').value;

    this.authService.login(this.loginRequestPayload).subscribe(
      data => {
        this.isError = false;

        // Original
        // this.router.navigateByUrl('/');

        // Modified (refresh after login)
        this.router.navigateByUrl('/').then(() => {
          window.location.reload();
        });

        this.toastr.success('Login successfully 😊');

        console.log('Login successfully');
      },
      error => {
        this.isError = true;
        this.toastr.error('Bad credentials, maybe try again 😉?');

        throwError(error);
      },
    );
  }
}

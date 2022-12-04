import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { SignupComponent } from './signup/signup.component';
import { ReactiveFormsModule } from '@angular/forms';
import { LoginComponent } from './login/login.component';
import { UserProfileComponent } from './user-profile/user-profile.component';

@NgModule({
  declarations: [SignupComponent, LoginComponent, UserProfileComponent],
  imports: [CommonModule, ReactiveFormsModule],
})
export class AuthModule {}

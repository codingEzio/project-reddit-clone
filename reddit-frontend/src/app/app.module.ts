import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { NgxWebstorageModule } from 'ngx-webstorage';
import { ToastrModule } from 'ngx-toastr';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';

import { ReactiveFormsModule } from '@angular/forms';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { HeaderComponent } from './header/header.component';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';
import { AuthModule } from './auth/auth.module';
import { TokenInterceptor } from './token-interceptor';
import { HomeComponent } from './home/home.component';
import { PostTileComponent } from './shared/post-tile/post-tile.component';
import { SideBarComponent } from './shared/side-bar/side-bar.component';
import { SubredditSideBarComponent } from './shared/subreddit-side-bar/subreddit-side-bar.component';
import { VoteButtonComponent } from './shared/vote-button/vote-button.component';
import { CreateSubredditComponent } from './subreddit/create-subreddit/create-subreddit.component';
import { CreatePostComponent } from './post/create-post/create-post.component';
import { ListSubredditsComponent } from './subreddit/list-subreddits/list-subreddits.component';
import { EditorModule } from '@tinymce/tinymce-angular';
import { ViewPostComponent } from './post/view-post/view-post.component';

@NgModule({
  declarations: [
    AppComponent,
    HeaderComponent,
    HomeComponent,
    PostTileComponent,
    SideBarComponent,
    SubredditSideBarComponent,
    VoteButtonComponent,
    CreateSubredditComponent,
    CreatePostComponent,
    ListSubredditsComponent,
    ViewPostComponent,
  ],
  imports: [
    BrowserModule,
    FontAwesomeModule,
    ReactiveFormsModule,
    AppRoutingModule,
    HttpClientModule,
    AuthModule,
    NgxWebstorageModule.forRoot(),
    BrowserAnimationsModule,
    ToastrModule.forRoot(),
    EditorModule,
  ],
  providers: [
    {
      provide: HTTP_INTERCEPTORS,
      useClass: TokenInterceptor,
      multi: true,
    },
  ],
  bootstrap: [AppComponent],
})
export class AppModule {}

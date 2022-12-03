import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { HttpClient } from '@angular/common/http';

import { SubredditModel } from './subreddit-model';

@Injectable({
  providedIn: 'root',
})
export class SubredditService {
  constructor(private http: HttpClient) {}

  getAllSubreddits(): Observable<Array<SubredditModel>> {
    return this.http.get<Array<SubredditModel>>(
      'http://localhost:8080/api/subreddit/',
    );
  }
}
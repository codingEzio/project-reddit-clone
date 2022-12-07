import { Component, OnInit, ViewEncapsulation, Input } from '@angular/core';
import { Router } from '@angular/router';
import { faComments } from '@fortawesome/free-solid-svg-icons';

import { PostService } from '../post.service';
import { PostModel } from '../post-model';

@Component({
  selector: 'app-post-tile',
  templateUrl: './post-tile.component.html',
  styleUrls: ['./post-tile.component.css'],
  encapsulation: ViewEncapsulation.None,
})
export class PostTileComponent implements OnInit {
  @Input() posts: PostModel[];

  faComments = faComments;

  constructor(private router: Router) {}

  // constructor(private router: Router) {}

  ngOnInit(): void {}

  goToPost(id: number): void {
    this.router.navigateByUrl('/view-post/' + id);
  }
}

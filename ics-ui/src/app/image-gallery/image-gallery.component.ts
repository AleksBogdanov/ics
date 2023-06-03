import { Component, OnInit } from '@angular/core';
import { ImageService } from '../image.service';
import { ActivatedRoute, Router } from '@angular/router';
import { FormControl } from '@angular/forms';
import { debounceTime } from 'rxjs/operators';
import { Image, Tag, ImagePage } from '../image.model';  // Update this import as per your actual Image model path

@Component({
  selector: 'app-gallery',
  templateUrl: './image-gallery.component.html',
  styleUrls: ['./image-gallery.component.scss'],
})
export class GalleryComponent implements OnInit {
  images: Image[] = [];
  tagFormControl = new FormControl();
  inputFormControl = new FormControl();
  isAscending = true;
  sortButtonText = 'Ascending';

  page: number = 0;
  size: number = 10;

  constructor(
    private imageService: ImageService,
    private route: ActivatedRoute,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.inputFormControl.valueChanges
      .pipe(debounceTime(500))
      .subscribe((tag: string) => {
        this.filterImagesByTag(tag, null);
        this.tagFormControl.setValue(tag, { emitEvent: false });
      });
  
    this.route.queryParamMap.subscribe((params) => {
      const tag = params.get('tag');
      this.tagFormControl.setValue(tag, { emitEvent: false });
      this.inputFormControl.setValue(tag, { emitEvent: false });
  
      if (tag) {
        this.loadImagesByTag(tag);
      } else {
        this.loadDefaultImages();
      }
    });
  }
  
  
  loadDefaultImages(): void {
    const sort = this.isAscending ? 'asc' : 'desc';
    const page = 0;
    const size = 10;
  
    this.imageService.getImages(page, size, sort).subscribe(
      (page: ImagePage) => {
        this.images = page.content;
      },
      (error: any) => {
        console.error(error);
      }
    );
  }
  
  

  loadPrevious(): void {
    if (this.page > 0) {
      this.page--;
      this.loadImages();
    }
  }
  
  loadNext(): void {
    this.page++;
    this.loadImages();
  }

  sortImages(): void {
    this.isAscending = !this.isAscending;
    this.page = 0;
    console.log('Sorting direction: ' + (this.isAscending ? 'asc' : 'desc'));
    this.loadImages();
  }


loadImages(): void {
  console.log(this.page);
  this.imageService.getImages(this.page, this.size, this.isAscending ? 'asc' : 'desc').subscribe(
    (page: ImagePage) => {
      this.images = page.content; 
    },
    (error: any) => {
      console.error(error);
    }
  );
}
loadImagesByTag(tag: string | null): void {
  if (tag) {
    console.log('Searching with Tag: ' + tag);

    this.imageService.getImageByTag(tag).subscribe(
      (images) => {
        this.images = images;
      },
      (error) => {
        console.error(error);
      }
    );
  } else {
    this.imageService.getAllImages().subscribe(
      (images) => {
        console.log('All images');
        this.images = images;
      },
      (error) => {
        console.error(error);
      }
    );
  }
}

public filterImagesByTag(tag: string, event: Event | null): void {
  if (event) {
    event.stopPropagation();
  }

  if (tag) {
    console.log(tag);
    this.router.navigate([], {
      relativeTo: this.route,
      queryParams: { tag: tag },
      queryParamsHandling: 'merge',
    });
  } else {
    this.router.navigate(['/gallery']); // Navigate to the gallery route without any query parameters
  }
}
}

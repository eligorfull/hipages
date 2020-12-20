import {Component, ViewChild} from '@angular/core';
import {JobsComponent} from "./jobs/jobs.component";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent {
  title = 'hipages';
  @ViewChild('newTab') childNewTab: JobsComponent;
  @ViewChild('acceptedTab') childAcceptedTab: JobsComponent;

  tabClick(tab) {
    if (tab.index === 0) {
      this.childNewTab.getJobsByStatusFromServer('new');
    } else {
      this.childAcceptedTab.getJobsByStatusFromServer('accepted');
    }
  }
}

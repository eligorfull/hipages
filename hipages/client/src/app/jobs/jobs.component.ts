import {Component, Input, OnInit} from '@angular/core';
import {JobService} from "../service/job.service";
import {Job} from "../model/job";

@Component({
  selector: 'app-jobs',
  templateUrl: './jobs.component.html',
  styleUrls: ['./jobs.component.scss']
})
export class JobsComponent implements OnInit {

  @Input() jobStatus: any;
  jobs: Job[];
  constructor(private jobService: JobService) { }

  ngOnInit(): void {
    this.getJobsByStatusFromServer(this.jobStatus);
  }

  getJobsByStatusFromServer(jobStatus: string) {
    this.jobService.getAllNewJobs(jobStatus).subscribe(returnedNewJobs => {
      this.jobs = returnedNewJobs;
    }, err => {
      console.error("An error occurred while retrieving the leads");
    });
  }

  onJobUpdatedEvent($event) {
    if (this.jobs && this.jobs.length > 0) {
      let index = 0;
      for (const currJob of this.jobs) {
        if (currJob.id === $event) {
          this.jobs.splice(index, 1);
        }
        index++;
      }
    }
  }

}

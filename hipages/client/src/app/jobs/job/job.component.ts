import {Component, Input, OnInit, Output, EventEmitter} from '@angular/core';
import {Job} from "../../model/job";
import {JobService} from "../../service/job.service";

@Component({
  selector: 'app-job',
  templateUrl: './job.component.html',
  styleUrls: ['./job.component.scss']
})
export class JobComponent implements OnInit {

  @Input() job: Job;
  @Output() jobUpdatedEvent = new EventEmitter<number>();
  constructor(private jobService: JobService) { }

  ngOnInit(): void {
  }

  onAcceptJob() {
    this.jobService.changeJobStatus(this.job.id, 'accepted').subscribe(returnedJob => {
      this.job = returnedJob;
      this.jobUpdatedEvent.emit(this.job.id);
    }, err => {
      console.error("An error occurred while changing the job status to accepted");
    });
  }

  onDeclineJob() {
    this.jobService.changeJobStatus(this.job.id, 'declined').subscribe(returnedJob => {
      this.job = returnedJob;
      this.jobUpdatedEvent.emit(this.job.id);
    }, error => {
      console.error("An error occurred while changing the job status to declined");
    });
  }

}

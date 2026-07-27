import { Component, Input } from "@angular/core";

type IconName = 'view' | 'hide';

@Component({
    selector: 'app-icon',
    templateUrl: './icon.html',
    styleUrl: './icon.scss'
})
export class Icon {
    @Input() name: IconName = 'view';
}
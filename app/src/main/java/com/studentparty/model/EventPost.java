/*
 * Copyright 2016 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.studentparty.model;

import java.io.Serializable;

public class EventPost implements Serializable {
    private People author;
    private String eventTittle;
    private String eventDetail;
    private String eventImage;
    private String eventVideo;
    private String eventDate;
    private String eventLocation;
    private Object timestamp;

    public People getAuthor() {
        return author;
    }

    public void setAuthor(People author) {
        this.author = author;
    }

    public String getEventTittle() {
        return eventTittle;
    }

    public void setEventTittle(String eventTittle) {
        this.eventTittle = eventTittle;
    }

    public String getEventDetail() {
        return eventDetail;
    }

    public void setEventDetail(String eventDetail) {
        this.eventDetail = eventDetail;
    }

    public String getEventImage() {
        return eventImage;
    }

    public void setEventImage(String eventImage) {
        this.eventImage = eventImage;
    }

    public String getEventVideo() {
        return eventVideo;
    }

    public void setEventVideo(String eventVideo) {
        this.eventVideo = eventVideo;
    }

    public String getEventDate() {
        return eventDate;
    }

    public void setEventDate(String eventDate) {
        this.eventDate = eventDate;
    }

    public String getEventLocation() {
        return eventLocation;
    }

    public void setEventLocation(String eventLocation) {
        this.eventLocation = eventLocation;
    }

    public Object getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Object timestamp) {
        this.timestamp = timestamp;
    }

    public EventPost() {
        // empty default constructor, necessary for Firebase to be able to deserialize blog posts
    }

    public EventPost(People author, String eventTle, String eventDetail, String eventImage, String eventVideo, String date, String location, Object timestamp) {
        this.author = author;
        this.eventTittle = eventTle;
        this.eventDetail = eventDetail;
        this.eventImage = eventImage;
        this.eventVideo = eventVideo;
        this.eventDate = date;
        this.eventLocation = location;
        this.timestamp=timestamp;

    }


}

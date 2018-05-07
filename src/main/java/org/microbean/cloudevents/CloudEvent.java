/* -*- mode: Java; c-basic-offset: 2; indent-tabs-mode: nil; coding: utf-8-unix -*-
 *
 * Copyright © 2018 microBean.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied.  See the License for the specific language governing
 * permissions and limitations under the License.
 */
package org.microbean.cloudevents;

import java.io.Serializable; // for javadoc only

import java.net.URI;

import java.time.Instant;

import java.util.Collections;
import java.util.EventObject;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

import org.microbean.development.annotation.Experimental;

/**
 * An {@link EventObject} implementing the <a
 * href="https://github.com/cloudevents/spec/blob/v0.1/spec.md">CloudEvents
 * specification, version 0.1</a>.
 *
 * @author <a href="https://about.me/lairdnelson"
 * target="_parent">Laird Nelson</a>
 *
 * @see <a
 * href="https://github.com/cloudevents/spec/blob/v0.1/spec.md">The
 * CloudEvents specification, version 0.1</a>
 */
@Experimental
public class CloudEvent extends EventObject {

  /**
   * The version of this class for {@linkplain Serializable
   * serialization purposes}.
   */
  private static final long serialVersionUID = 1L;

  private static final Object UNKNOWN_PUBLISHER = new Object();

  private volatile Object publisher;

  private final String cloudEventsVersion;

  /**
   * The specification is unclear as to whether this identifies the
   * type of the event or the type of the occurrence being described.
   */
  private final String eventType;

  /**
   * The specification is unclear as to whether this identifies the
   * version of the type of event or the version of the type of
   * occurrence being described.
   */
  private final String eventTypeVersion;

  private final URI source;

  /**
   * The specification is unclear as to whether this identifies an
   * event or the occurrence that it describes.
   */
  private final String eventID;

  /**
   * The specification is unclear as to whether this identifies the
   * time the event was dispatched, or the time the occurrence it
   * describes occurred.
   */
  private final Instant eventTime;

  private final Map<String, Object> extensions;

  private final String contentType; // e.g. application/foobar+json or the like

  private final URI schemaURL;
  
  private final Object data;

  public CloudEvent(final Object publisher,
                    final String cloudEventsVersion,
                    final String eventType,
                    final String eventTypeVersion,
                    final URI source,
                    final String eventID,
                    final Instant eventTime,
                    final Object data) {
    this(publisher, cloudEventsVersion, eventType, eventTypeVersion, source, eventID, eventTime, null, null, null, data);
  }

  public CloudEvent(final String cloudEventsVersion,
                    final String eventType,
                    final String eventTypeVersion,
                    final URI source,
                    final String eventID,
                    final Instant eventTime,
                    final Map<? extends String, ?> extensions,
                    final String contentType,
                    final URI schemaURL,
                    final Object data) {
    this(null, cloudEventsVersion, eventType, eventTypeVersion, source, eventID, eventTime, extensions, contentType, schemaURL, data);
  }
  
  public CloudEvent(final Object publisher,
                    final String cloudEventsVersion,
                    final String eventType,
                    final String eventTypeVersion,
                    final URI source,
                    final String eventID,
                    final Instant eventTime,
                    final Map<? extends String, ?> extensions,
                    final String contentType,
                    final URI schemaURL,
                    final Object data) {
    super(publisher == null ? UNKNOWN_PUBLISHER : publisher);
    this.cloudEventsVersion = Objects.requireNonNull(cloudEventsVersion);
    if (cloudEventsVersion.isEmpty()) {
      throw new IllegalArgumentException("cloudEventsVersion: ");
    }
    this.eventType = Objects.requireNonNull(eventType);
    if (eventType.isEmpty()) {
      throw new IllegalArgumentException("eventType: ");
    }
    this.eventTypeVersion = eventTypeVersion;
    if (eventTypeVersion != null && eventTypeVersion.isEmpty()) {
      throw new IllegalArgumentException("eventTypeVersion: ");
    }    
    this.source = Objects.requireNonNull(source);
    this.eventID = Objects.requireNonNull(eventID);
    if (eventID.isEmpty()) {
      throw new IllegalArgumentException("eventID: ");
    }
    this.eventTime = eventTime == null ? Instant.now() : eventTime;
    if (extensions == null) {
      this.extensions = null;
    } else {
      synchronized (extensions) {
        if (extensions.isEmpty()) {
          this.extensions = Collections.emptyMap();
        } else {
          this.extensions = Collections.unmodifiableMap(new LinkedHashMap<>(extensions));
        }
      }
    }
    this.contentType = contentType;
    this.schemaURL = schemaURL;
    this.data = data;
  }

  public final Object getPublisher() {
    Object returnValue;
    final Object publisher = this.publisher;
    if (publisher == null) {
      returnValue = super.getSource();
      if (returnValue == UNKNOWN_PUBLISHER) {
        returnValue = null;
      }
    } else {
      returnValue = publisher;
    }
    return returnValue;
  }

  public final void setPublisher(final Object publisher) {
    Objects.requireNonNull(publisher);
    final Object existingPublisher = this.getPublisher();
    if (existingPublisher != null && existingPublisher != UNKNOWN_PUBLISHER) {
      throw new IllegalStateException();
    }
    this.publisher = publisher;
  }
  
  public final String getCloudEventsVersion() {
    return this.cloudEventsVersion;
  }

  public final String getEventType() {
    return this.eventType;
  }

  public final String getEventTypeVersion() {
    return this.eventTypeVersion;
  }

  @Override
  public final URI getSource() {
    return this.source;
  }

  public final String getEventID() {
    return this.eventID;
  }

  public final Instant getEventTime() {
    return this.eventTime;
  }

  public final Map<String, Object> getExtensions() {
    return this.extensions;
  }

  public final String getContentType() {
    return this.contentType;
  }

  public final URI getSchemaURL() {
    return this.schemaURL;
  }

  public final Object getData() {
    return this.data;
  }
    
}

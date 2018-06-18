package com.playground

import com.playground.context._

trait Application
  extends LoggerContextComponent
    with ActorContextComponent
    with ConfigContextComponent
    with SlickContextComponent

class DefaultApplication
  extends Application
    with DefaultLoggerContextComponent
    with DefaultActorContextComponent
    with DefaultConfigContextComponent 
    with DefaultSlickContextComponent

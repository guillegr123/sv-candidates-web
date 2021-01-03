package com.its.svcandidates2021.config

case class Sensitive(value: String) extends AnyVal {
  override def toString: String = "***"
}

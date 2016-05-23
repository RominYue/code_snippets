package com.clearcut.nlp

case class SentenceParseResult(
    sentence: String,
    words: List[String],
    pos_tags: List[String],
    ner_tags: List[String],
    dep_labels: List[String],
    dep_parents: List[Int]
)

case class DocumentParseResult(
    sentences: List[SentenceParseResult]
)

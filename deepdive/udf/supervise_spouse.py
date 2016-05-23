#!/usr/bin/env python
#-*- coding: utf-8 -*-
import sys
reload(sys)
sys.setdefaultencoding('utf8')
from deepdive import *
import random
from collections import namedtuple
from utils import transform_ner_tags

SpouseLabel = namedtuple('SpouseLabel', 'p1_id, p2_id, label, type')

@tsv_extractor
@returns(lambda
        p1_id   = "text",
        p2_id   = "text",
        label   = "int",
        rule_id = "text",
    :[])
# heuristic rules for finding positive/negative examples of spouse relationship mentions
def supervise(
        p1_id="text", p1_begin="int", p1_end="int",
        p2_id="text", p2_begin="int", p2_end="int",
        doc_id="text", sentence_index="int", sentence_text="text",
        tokens="text[]", pos_tags="text[]", ner_tags="text[]",
        dep_types="text[]", dep_token_indexes="int[]",
    ):
    
    ner_tags = transform_ner_tags(ner_tags)
    # Constants
    MARRIED = frozenset([u"妻子", u"丈夫", u"老公", u"老婆", u"情侣"])
    FAMILY = frozenset([u"父亲", u"母亲", u"姐姐", u"哥哥", u"侄子", u"爸爸", u"妈妈", u"姐弟", u"姐妹"])
    MAX_DIST = 10

    # Common data objects
    p1_end_idx = min(p1_end, p2_end)
    p2_start_idx = max(p1_begin, p2_begin)
    p2_end_idx = max(p1_end,p2_end)
    intermediate_tokens = tokens[p1_end_idx+1:p2_start_idx]
    intermediate_ner_tags = ner_tags[p1_end_idx+1:p2_start_idx]
    tail_tokens = tokens[p2_end_idx+1:]
    spouse = SpouseLabel(p1_id=p1_id, p2_id=p2_id, label=None, type=None)

    # Rule: Candidates that are too far apart
    if len(intermediate_tokens) > MAX_DIST:
        yield spouse._replace(label=-1, type='neg:far_apart')

    # Rule: Candidates that have a third person in between
    if 'PERSON' in intermediate_ner_tags:
        yield spouse._replace(label=-1, type='neg:third_person_between')

    # Rule: Sentences that contain wife/husband in between
    #         (<P1>)([ A-Za-z]+)(wife|husband)([ A-Za-z]+)(<P2>)
    if len(MARRIED.intersection(intermediate_tokens)) > 0:
        yield spouse._replace(label=1, type='pos:wife_husband_between')

    # Rule: Sentences that contain and ... married
    #         (<P1>)(and)?(<P2>)([ A-Za-z]+)(married)
    if (u"和" in intermediate_tokens) and (u"结婚" in tail_tokens):
        yield spouse._replace(label=1, type='pos:married_after')

    # Rule: Sentences that contain familial relations:
    #         (<P1>)([ A-Za-z]+)(brother|stster|father|mother)([ A-Za-z]+)(<P2>)
    if len(FAMILY.intersection(intermediate_tokens)) > 0:
        yield spouse._replace(label=-1, type='neg:familial_between')

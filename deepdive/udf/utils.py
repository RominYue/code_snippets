#-*-coding=utf8-*-

def transform_ner_tags(ner_tags):
    new_ner_tags = []
    for x in ner_tags:
        if x.endswith('Nh'):
            new_ner_tags.append('PERSON')
        elif x.endswith('Ni'):
            new_ner_tags.append('ORGNIZATION')
        elif x.endswith('Ns'):
            new_ner_tags.append('PLACE')
        else:
            new_ner_tags.append('O')
    return new_ner_tags


if __name__ == '__main__':
    test = ["S-Nh","O","O","O","S-Nh","O","O","O"]
    a = transform_ner_tags(test)
    print a

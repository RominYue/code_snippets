#coding=utf8
'''
Created on Feb 14, 2016

@author: myue
'''

import sys
import functools
import traceback
import time
import logging
import logConf

_LOGGER = logging.getLogger('test')
_LOGGER_PERF = logging.getLogger('perf')

def exception(method):
    '''Decorate methods with this to handle exception.
    '''
    @functools.wraps(method)
    def wrapper(arg):
        '''wrapper
        '''
        try:
            return method(arg)
        except Exception:
            exc_type, value, details = sys.exc_info()
            formatted_tb = traceback.format_tb(details)
            message = '%s: %s traceback=%s' % (exc_type, value, formatted_tb)
            _LOGGER.exception(message)
    return wrapper

#decorator to test function runtime
def performance(method):
    '''Decorate methods with this to handle performance logging.
    '''
    @functools.wraps(method)
    def wrapper(arg, *args, **kwargs):
        '''wrapper
        '''
        start_time = time.time()
        try:
            return method(arg, *args, **kwargs)
        except:
            raise
        finally:
            module_name = method.func_code.co_filename.split("/")[-1].split('.')[0]
            fname = method.func_name
            perf = (time.time() - start_time) * 1000
            _LOGGER_PERF.info('%s.%s %.4f ms' % (module_name, fname, perf))
            #print "%s.%s %.4f ms" % (module_name, fname, perf)
    return wrapper

@exception
@performance
def test_for(a):
    for i in range(100):
        pass
    #b = 1/0
    time.sleep(1)

if __name__ == '__main__':
    a=1
    test_for(a)

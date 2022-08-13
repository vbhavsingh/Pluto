/* ng-infinite-scroll - v1.0.0 - 2013-02-23 */
var mod;

mod = angular.module('infinite-scroll', []);

mod.directive('infiniteScroll', [ '$rootScope', '$window', '$timeout','$document', function($rootScope, $window, $timeout,$document) {
    return {
      link: function(scope, elem, attrs) {
        var checkWhenEnabled, handler, scrollDistance, scrollEnabled;
        $window = angular.element($window);
        scrollDistance = 0;
        scrollPreviousAt=0;
        scrollTriggredAt=0;
        if (attrs.infiniteScrollDistance != null) {
          scope.$watch(attrs.infiniteScrollDistance, function(value) {
            return scrollDistance = parseFloat(value, 10);
          });
        }
        scrollEnabled = true;
        checkWhenEnabled = false;
        if (attrs.infiniteScrollDisabled != null) {
          scope.$watch(attrs.infiniteScrollDisabled, function(value) {
            scrollEnabled = !value;
            if (scrollEnabled && checkWhenEnabled) {
              checkWhenEnabled = false;
              return handler();
            }
          });
        }
        handler = function() {
          if($rootScope.reload){
        	  scrollDistance = 0;
              scrollPreviousAt=0;
              scrollTriggredAt=0;
              $rootScope.reload=false;
          }
          if($window.scrollTop() <=  scrollPreviousAt) {
        	  scrollPreviousAt=$window.scrollTop();
        	  return false;
          }
          if($window.scrollTop()- scrollTriggredAt < ($window.height() * scrollDistance)) {
        	  scrollPreviousAt=$window.scrollTop();
        	  return false;
          }
          var thisElement=document.getElementById($rootScope.currentTab);
         // alert(thisElement);
          scrollPreviousAt=$window.scrollTop();
          var elementBottom, remaining, shouldScroll, windowBottom;       
          windowBottom = $window.height() + $window.scrollTop();
          //elementBottom = elem.offset().top + thisElement.height();
          elementBottom = thisElement.clientHeight;
          
          
          remaining = elementBottom - windowBottom;
         // console.log("offset top="+elem.offset().top+", thisElement="+thisElement.clientHeight+", windowBottom="+windowBottom+", elementBottom="+elementBottom+", remaining="+remaining);
          shouldScroll = remaining <= $window.height() * scrollDistance;
          if (shouldScroll && scrollEnabled) {
        	scrollTriggredAt =$window.scrollTop();
            if ($rootScope.$$phase) {
              return scope.$eval(attrs.infiniteScroll);
            } else {
              return scope.$apply(attrs.infiniteScroll);
            }
          } else if (shouldScroll) {
            return checkWhenEnabled = true;
          }
        };
        $window.on('scroll', handler);
        scope.$on('$destroy', function() {
          return $window.off('scroll', handler);
        });
        return $timeout((function() {
          if (attrs.infiniteScrollImmediateCheck) {
            if (scope.$eval(attrs.infiniteScrollImmediateCheck)) {
              return handler();
            }
          } else {
            return handler();
          }
        }), 0);
      }
    };
  }
]);

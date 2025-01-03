from django.urls import path
from .views import HelloWorldAPI

urlpatterns = [
    path('api/hello/', HelloWorldAPI.as_view(), name='hello-api'),
]
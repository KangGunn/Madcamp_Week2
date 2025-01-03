from django.shortcuts import render

# Create your views here.
from rest_framework.views import APIView
from rest_framework.response import Response
from django.http import HttpResponse

def home(request):
    return HttpResponse("<h1>Welcome!</h1>")

class HelloWorldAPI(APIView):
    def get(self, request):
        return Response({"message": "Hello, Django REST Framework!?!"})
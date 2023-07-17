terraform {
  backend "remote" {
    organization = "yogi_12345"

    workspaces {
      name = "ToyStore"
    }
  }
}

resource "null_resource" "example" {
  triggers = {
    value = "An example resource that does nothing!"
  }
}

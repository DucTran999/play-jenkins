package calc_test

import (
	"fmt"
	"testing"

	"github.com/jarcoal/httpmock"
)

func TestFetchArticles(t *testing.T) {
	httpmock.Activate()
	t.Cleanup(httpmock.DeactivateAndReset)

	// Exact URL match
	httpmock.RegisterResponder("GET", "https://api.mybiz.com/articles",
		httpmock.NewStringResponder(200, `[{"id": 1, "name": "My Great Article"}]`))

	// Regexp match (could use httpmock.RegisterRegexpResponder instead)
	httpmock.RegisterResponder("GET", `=~^https://api\.mybiz\.com/articles/id/\d+\z`,
		httpmock.NewStringResponder(200, `{"id": 1, "name": "My Great Article"}`))

	// get count info
	httpmock.GetTotalCallCount()

	// get the amount of calls for the registered responder
	info := httpmock.GetCallCountInfo()
	a := info["GET https://api.mybiz.com/articles"] // number of GET calls made to https://api.mybiz.com/articles
	fmt.Println(a)

	// info["GET https://api.mybiz.com/articles/id/12"]         // number of GET calls made to https://api.mybiz.com/articles/id/12
	// info[`GET =~^https://api\.mybiz\.com/articles/id/\d+\z`] // number of GET calls made to https://api.mybiz.com/articles/id/<any-number>
}
